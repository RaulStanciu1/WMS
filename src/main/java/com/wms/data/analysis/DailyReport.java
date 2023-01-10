package com.wms.data.analysis;

public class DailyReport {
    private final float deniedRequestsPercentage;
    private final float approvedRequestsPercentage;
    private final float pendingRequestsPercentage;
    private String recommendedActions;
    public DailyReport(float deniedRequestsPercentage,float approvedRequestsPercentage,float pendingRequestsPercentage){
        this.approvedRequestsPercentage=approvedRequestsPercentage;
        this.deniedRequestsPercentage=deniedRequestsPercentage;
        this.pendingRequestsPercentage=pendingRequestsPercentage;
    }

    public void analyzeReport(){
        if(pendingRequestsPercentage>approvedRequestsPercentage+deniedRequestsPercentage){
            this.recommendedActions="Possible Inactivity In The Workforce. Intervention Required!";
        }
        if(approvedRequestsPercentage>80){
            this.recommendedActions="No Recommended Actions Required";
        }
        if(deniedRequestsPercentage>25){
            this.recommendedActions="Possible Malicious Intent In The Workforce. Intervention Required!";
        }
        if(pendingRequestsPercentage==0 && approvedRequestsPercentage==0 && deniedRequestsPercentage==0){
            this.recommendedActions="Requests have not been sent yet. Check later in the day for more information.";
        }
    }

    public float getApprovedRequestsPercentage() {
        return approvedRequestsPercentage;
    }

    public float getDeniedRequestsPercentage() {
        return deniedRequestsPercentage;
    }

    public float getPendingRequestsPercentage() {
        return pendingRequestsPercentage;
    }
    public String getRecommendedActions(){
        return recommendedActions;
    }
}
