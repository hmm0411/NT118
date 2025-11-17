package course.examples.cinepople.domain;

public class DateModel {
    private String dayOfWeek;
    private String date;
    private boolean isSelected;

    public DateModel(){}
    public DateModel(String dayOfWeek, String date, boolean isSelected) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.isSelected = isSelected;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDate() {
        return date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}