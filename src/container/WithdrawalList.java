package container;
import entity.*;
import utils.CSVReader;
import utils.CSVWriter;
import utils.Colour;
import java.util.ArrayList;
import java.util.List;

public class WithdrawalList {
    private List<Withdrawal> withdrawals;
    private ApplicationList applicationList;
    private ApplicantList applicantList;
    private ProjectList projectList;

    public WithdrawalList() {
        this.withdrawals = new ArrayList<>();
    }

    public WithdrawalList(String filepath, ApplicationList applicationList, ApplicantList applicantList, ProjectList projectList){
        this.withdrawals = new ArrayList<>();
        this.applicationList = applicationList;
        this.applicantList = applicantList;
        this.projectList = projectList;
        loadWithdrawals(filepath);
    }

    private void loadWithdrawals(String filePath) {
    List<String[]> data = CSVReader.readCSV(filePath);
    for(String[] row : data) {
        String applicantNric = row[0];
        String projectName = row[1];
        String status = row[2]; 

        Applicant applicant = applicantList.getApplicantByNric(applicantNric);
        Project project = projectList.getProjectByName(projectName);

        if (applicant != null && project != null) {
            Application application = applicationList.getApplicationByApplicant(applicant);
            if (application != null && application.getProject().equals(project)) {
                Withdrawal withdrawal = new Withdrawal(application);
                withdrawal.setWithdrawalStatus(Withdrawal.WithdrawalStatus.valueOf(status));
                withdrawals.add(withdrawal);
                }
            }
        }
    }

    public void addWithdrawal(Withdrawal withdrawal) {
        withdrawals.add(withdrawal);
        System.out.println(Colour.GREEN + "Withdrawal request submitted." + Colour.RESET);
    }

    public void viewWithdrawalsByApplicant(Applicant applicant) {
        System.out.println("\n===== My Withdrawal Requests =====");
        for (Withdrawal withdrawal : withdrawals) {
            if (withdrawal.getApplication().getApplicant().equals(applicant)) {
                System.out.println(withdrawal);
            }
        }
    }

    public ArrayList<Withdrawal> getPendingWithdrawalsByProject(Project project) {
        ArrayList<Withdrawal> pending = new ArrayList<>();
        for (Withdrawal withdrawal : withdrawals) {
            Application app = withdrawal.getApplication();
            if (app.getProject().equals(project) &&
                withdrawal.getStatus() == Withdrawal.WithdrawalStatus.PENDING) {
                pending.add(withdrawal);
            }
        }
        return pending;
    }

    public void saveToCSV() {
    List<String[]> data = new ArrayList<>();
    data.add(new String[]{"applicantNric", "projectName", "status"});

    for (Withdrawal w : this.withdrawals) {
        data.add(new String[]{
            w.getApplication().getApplicant().getNric(),
            w.getApplication().getProject().getProjectName(),
            w.getStatus().name()
        });
    }

    CSVWriter.writeCSV("../data/WithdrawalList.csv", data);
}

}