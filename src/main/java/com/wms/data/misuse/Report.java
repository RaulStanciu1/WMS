package com.wms.data.misuse;

import java.time.LocalDate;
import java.util.List;

public class Report {
    List<Misuse> inconsistencies;
    List<String> possibleActions;
    LocalDate date;
    public Report(List<Misuse> inconsistencies,List<String> possibleActions,LocalDate date){
        this.inconsistencies=inconsistencies;
        this.date=date;
        this.possibleActions=possibleActions;
    }

    public List<Misuse> getInconsistencies() {
        return inconsistencies;
    }


    public List<String> getPossibleActions() {
        return possibleActions;
    }

    public LocalDate getDate() {
        return date;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Report Date: ").append(date).append("\n");
        if(this.getInconsistencies().size()==0){
            str.append("Report Has No Incidents");
        }
        for(int i=0;i<this.getInconsistencies().size();i++){
            str.append("***\n");
            str.append("INCIDENT #").append(i+1).append("\n")
                    .append(inconsistencies.get(i)).append("\n   Possible Action:").append(possibleActions.get(i))
                    .append("\n");
            str.append("***").append("\n");
        }
        return str.toString();
    }
}
