import com.example.attendify.data.model.Subject


data class SubjectWithAttendance(
    val subject: Subject,
    val attendancePercentage: Int,
    val subjectCode: String
)
