import android.content.Context
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

suspend fun generateExcelReport(
    context: Context,
    reportData: Map<String, Int>,  // studentEmail -> attendancePercentage
    subjectName: String
): String? = withContext(Dispatchers.IO) {
    try {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Attendance Report")

        // Header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Student Email")
        headerRow.createCell(1).setCellValue("Attendance (%)")

        // Data rows
        var rowIndex = 1
        for ((email, percentage) in reportData) {
            val row = sheet.createRow(rowIndex++)
            row.createCell(0).setCellValue(email)
            row.createCell(1).setCellValue(percentage.toDouble())
        }

        // Set fixed column widths (in units of 1/256th of a character)
        sheet.setColumnWidth(0, 30 * 256)  // Student Email column wide enough
        sheet.setColumnWidth(1, 15 * 256)  // Attendance percentage column

        // Prepare file path in external storage Downloads directory
        val fileName = "${subjectName}_Attendance_Report.xlsx"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        // Write to file
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }
        workbook.close()

        return@withContext file.absolutePath

    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Failed to generate Excel report: ${e.message}", Toast.LENGTH_LONG).show()
        }
        return@withContext null
    }
}
