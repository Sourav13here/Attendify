import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.attendify.data.model.AttendanceWithStudentInfo
import com.example.attendify.data.model.Subject
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

fun generateExcelReport(
    context: Context,
    attendanceList: List<AttendanceWithStudentInfo>,
    subject: Subject
): File? {
    return try {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Attendance Report")

        // === Cell Styles ===
        val centerStyle: CellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
        }

        val headerStyle: XSSFCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            setFont(workbook.createFont().apply {
                bold = true
            } as XSSFFont)
        }

        val titleStyle: XSSFCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            setFont(workbook.createFont().apply {
                fontHeightInPoints = 14
                bold = true
            } as XSSFFont)
        }

        val redStyle: XSSFCellStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            fillForegroundColor = IndexedColors.RED.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(workbook.createFont().apply {
                color = IndexedColors.WHITE.index
                bold = true
            } as XSSFFont)
        }

        // === Extract month info ===
        val allDates = attendanceList.flatMap { it.attendanceMap.keys }.distinct().sorted()
        Log.e("excel", "$allDates")
        val monthDateMap = allDates.groupBy { date ->
            val parts = date.split("-")
            if (parts.size >= 2) "${parts[0]}-${parts[1]}" else "Unknown"
        }
        Log.e("excel", "$monthDateMap")

        val sortedMonthKeys = monthDateMap.keys.sorted()
        val firstMonth = sortedMonthKeys.firstOrNull()?.split("-")?.get(1)?.let { monthName(it) } ?: "Unknown"
        val lastMonth = sortedMonthKeys.lastOrNull()?.split("-")?.get(1)?.let { monthName(it) } ?: "Unknown"

        val dateOrder = mutableListOf<String>()

        // === Title Row ===
        val titleRow = sheet.createRow(0)
        titleRow.createCell(0).apply {
            setCellValue("Attendance Report ($firstMonth-$lastMonth)")
            cellStyle = titleStyle
        }
        val totalColumnsEstimate = 2 + allDates.size + 1
        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, totalColumnsEstimate))

        // === Branch & Semester Row ===
        val branchRow = sheet.createRow(1)
        branchRow.createCell(0).apply {
            setCellValue("${subject.branch} - ${subject.semester} sem")
            cellStyle = headerStyle as XSSFCellStyle?
        }
        sheet.addMergedRegion(CellRangeAddress(1, 1, 0, totalColumnsEstimate))

        // === Subject Row ===
        val subjectRow = sheet.createRow(2)
        subjectRow.createCell(0).apply {
            setCellValue("Subject: ${subject.subjectName}")
            cellStyle = headerStyle as XSSFCellStyle?
        }
        sheet.addMergedRegion(CellRangeAddress(2, 2, 0, totalColumnsEstimate))

        // === Empty Row after Subject ===
        val emptyRow = sheet.createRow(3)
        sheet.addMergedRegion(CellRangeAddress(3, 3, 0, totalColumnsEstimate))

        // === Header Rows ===
        val headerRow1 = sheet.createRow(4) // Month row
        val headerRow2 = sheet.createRow(5) // Date row

        headerRow1.createCell(0).apply {
            setCellValue("Roll No")
            cellStyle = headerStyle
        }
        headerRow1.createCell(1).apply {
            setCellValue("Name")
            cellStyle = headerStyle
        }
        headerRow2.createCell(0).apply { cellStyle = headerStyle }
        headerRow2.createCell(1).apply { cellStyle = headerStyle }

        var colIndex = 2
        for (monthKey in sortedMonthKeys) {
            val dates = monthDateMap[monthKey]?.sorted() ?: continue
            val shortMonth = monthName(monthKey.split("-")[1])

            if (dates.size > 1) {
                sheet.addMergedRegion(CellRangeAddress(4, 4, colIndex, colIndex + dates.size - 1))
            }

            headerRow1.createCell(colIndex).apply {
                setCellValue(shortMonth)
                cellStyle = headerStyle
            }

            for (date in dates) {
                val day = date.split("-")[2]
                headerRow2.createCell(colIndex).apply {
                    setCellValue(day)
                    cellStyle = headerStyle
                }
                dateOrder.add(date)
                colIndex++
            }
        }

        headerRow1.createCell(colIndex).apply {
            setCellValue("Percentage")
            cellStyle = headerStyle
        }
//        headerRow2.createCell(colIndex).apply {
//            setCellValue("%")
//            cellStyle = headerStyle
//        }

        // === Data Rows ===
        attendanceList.sortedBy { it.rollNumber }.forEachIndexed { rowIndex, attendance ->
            val row = sheet.createRow(rowIndex + 6)
            row.createCell(0).apply {
                setCellValue(attendance.rollNumber)
            }
            row.createCell(1).apply {
                setCellValue(attendance.name)
            }

            var presentCount = 0
            val totalClasses = attendance.attendanceMap.keys.size
            Log.e("excel" , "$attendance")
            dateOrder.forEachIndexed { i, date ->
                val status = attendance.attendanceMap[date]
                row.createCell(i + 2).apply {
                    setCellValue(if (status == 1) "P" else if (status == 0) "A" else "")
                    cellStyle = centerStyle as XSSFCellStyle?
                }
                if (status == 1) presentCount++
                Log.e("excel", "$status")
            }

            val percent = if (totalClasses > 0) (presentCount * 100) / totalClasses else 0
            row.createCell(2 + dateOrder.size).apply {
                setCellValue("$percent%")
                cellStyle = (if (percent < 65) redStyle else centerStyle) as XSSFCellStyle?
            }

        }

        // === Column Widths ===
        sheet.setColumnWidth(0, 4000)
        sheet.setColumnWidth(1, 7000)
        for (i in 2 until (dateOrder.size + 2)) {
            sheet.setColumnWidth(i, 1000)
        }
        sheet.setColumnWidth(dateOrder.size + 2, 3500)

        // === File Save ===
        val monthPart = sortedMonthKeys.joinToString("_") { monthName(it.split("-")[1]) }
        val baseFileName = "${subject.subjectName} ${subject.branch} ${subject.semester} ${monthPart} Attendance"
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val appFolder = File(downloadsPath, "Attendify")

        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }

    // Generate a unique file name
        var file: File
        var counter = 1
        do {
            val suffix = if (counter == 1) "" else "_$counter"
            val fileName = "$baseFileName$suffix.xlsx"
            file = File(appFolder, fileName)
            counter++
        } while (file.exists())

    // Write workbook to file
        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()
        Log.e("excel", "File path: ${file.absolutePath}")
        Log.e("excel", "File exists before writing: ${file.exists()}")

        return file

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



fun monthName(month: String): String {
    return when (month) {
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09" -> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"
        "12" -> "Dec"
        else -> "Unknown"
    }
}
