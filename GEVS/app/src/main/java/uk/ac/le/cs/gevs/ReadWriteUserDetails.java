package uk.ac.le.cs.gevs;

public class ReadWriteUserDetails {
    public String fullName, dob, constituency, uvc;

    public ReadWriteUserDetails(String textFullName, String textDob, String textConstituency, String textUVC) {
        this.fullName = textFullName;
        this.dob = textDob;
        this.constituency = textConstituency;
        this.uvc = textUVC;
    }
}
