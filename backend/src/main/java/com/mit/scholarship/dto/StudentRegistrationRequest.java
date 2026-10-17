package com.mit.scholarship.dto;

import com.mit.scholarship.model.Student;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StudentRegistrationRequest {

    private Student student;
    private BackgroundDetails background;
    private FinancialDetails financial;
    private EducationDetails education;
    private List<AcademicRecordDetails> academicRecords;

    public StudentRegistrationRequest() {
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public BackgroundDetails getBackground() {
        return background;
    }

    public void setBackground(BackgroundDetails background) {
        this.background = background;
    }

    public FinancialDetails getFinancial() {
        return financial;
    }

    public void setFinancial(FinancialDetails financial) {
        this.financial = financial;
    }

    public EducationDetails getEducation() {
        return education;
    }

    public void setEducation(EducationDetails education) {
        this.education = education;
    }

    public List<AcademicRecordDetails> getAcademicRecords() {
        return academicRecords;
    }

    public void setAcademicRecords(List<AcademicRecordDetails> academicRecords) {
        this.academicRecords = academicRecords;
    }

    public static class BackgroundDetails {
        private String community;
        private String religion;
        private Boolean isFirstGraduate;
        private Boolean isDifferentlyAbled;
        private Boolean isOrphan;
        private Boolean hasSingleParent;
        private String admissionMode;
        private Boolean isBeediCineWorkerDependent;
        private Boolean isCapfAssamRiflesDependent;

        public BackgroundDetails() {}

        public String getCommunity() { return community; }
        public void setCommunity(String community) { this.community = community; }

        public String getReligion() { return religion; }
        public void setReligion(String religion) { this.religion = religion; }

        public Boolean getIsFirstGraduate() { return isFirstGraduate; }
        public void setIsFirstGraduate(Boolean value) { this.isFirstGraduate = value; }

        public Boolean getIsDifferentlyAbled() { return isDifferentlyAbled; }
        public void setIsDifferentlyAbled(Boolean value) { this.isDifferentlyAbled = value; }

        public Boolean getIsOrphan() { return isOrphan; }
        public void setIsOrphan(Boolean value) { this.isOrphan = value; }

        public Boolean getHasSingleParent() { return hasSingleParent; }
        public void setHasSingleParent(Boolean value) { this.hasSingleParent = value; }

        public String getAdmissionMode() { return admissionMode; }
        public void setAdmissionMode(String admissionMode) { this.admissionMode = admissionMode; }

        public Boolean getIsBeediCineWorkerDependent() { return isBeediCineWorkerDependent; }
        public void setIsBeediCineWorkerDependent(Boolean value) { this.isBeediCineWorkerDependent = value; }

        public Boolean getIsCapfAssamRiflesDependent() { return isCapfAssamRiflesDependent; }
        public void setIsCapfAssamRiflesDependent(Boolean value) { this.isCapfAssamRiflesDependent = value; }
    }

    public static class FinancialDetails {
        private BigDecimal parentalAnnualIncome;
        private BigDecimal parentalMonthlyIncome;
        private Boolean incomeCertificateAvailable;

        public FinancialDetails() {}

        public BigDecimal getParentalAnnualIncome() { return parentalAnnualIncome; }
        public void setParentalAnnualIncome(BigDecimal value) { this.parentalAnnualIncome = value; }

        public BigDecimal getParentalMonthlyIncome() { return parentalMonthlyIncome; }
        public void setParentalMonthlyIncome(BigDecimal value) { this.parentalMonthlyIncome = value; }

        public Boolean getIncomeCertificateAvailable() { return incomeCertificateAvailable; }
        public void setIncomeCertificateAvailable(Boolean value) { this.incomeCertificateAvailable = value; }
    }

    public static class EducationDetails {
        private String class10InstitutionType;
        private String class12InstitutionType;
        private Boolean studiedGovClass6To12;
        private Boolean studiedGovClass10To12;
        private BigDecimal class10Percentage;
        private BigDecimal class12Percentage;
        private Integer class12CompletionYear;

        public EducationDetails() {}

        public String getClass10InstitutionType() { return class10InstitutionType; }
        public void setClass10InstitutionType(String value) { this.class10InstitutionType = value; }

        public String getClass12InstitutionType() { return class12InstitutionType; }
        public void setClass12InstitutionType(String value) { this.class12InstitutionType = value; }

        public Boolean getStudiedGovClass6To12() { return studiedGovClass6To12; }
        public void setStudiedGovClass6To12(Boolean value) { this.studiedGovClass6To12 = value; }

        public Boolean getStudiedGovClass10To12() { return studiedGovClass10To12; }
        public void setStudiedGovClass10To12(Boolean value) { this.studiedGovClass10To12 = value; }

        public BigDecimal getClass10Percentage() { return class10Percentage; }
        public void setClass10Percentage(BigDecimal value) { this.class10Percentage = value; }

        public BigDecimal getClass12Percentage() { return class12Percentage; }
        public void setClass12Percentage(BigDecimal value) { this.class12Percentage = value; }

        public Integer getClass12CompletionYear() { return class12CompletionYear; }
        public void setClass12CompletionYear(Integer value) { this.class12CompletionYear = value; }
    }

    public static class AcademicRecordDetails {
        private Integer semester;
        private BigDecimal cgpa;
        private BigDecimal attendancePercentage;
        private Integer arrearsCount;
        private Boolean conductGood;
        private String academicYear;

        public AcademicRecordDetails() {}

        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }

        public BigDecimal getCgpa() { return cgpa; }
        public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }

        public BigDecimal getAttendancePercentage() { return attendancePercentage; }
        public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }

        public Integer getArrearsCount() { return arrearsCount; }
        public void setArrearsCount(Integer arrearsCount) { this.arrearsCount = arrearsCount; }

        public Boolean getConductGood() { return conductGood; }
        public void setConductGood(Boolean conductGood) { this.conductGood = conductGood; }

        public String getAcademicYear() { return academicYear; }
        public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    }
}
