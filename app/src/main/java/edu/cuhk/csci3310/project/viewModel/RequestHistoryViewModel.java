package edu.cuhk.csci3310.project.viewModel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;

import edu.cuhk.csci3310.project.database.Status;

public class RequestHistoryViewModel extends ViewModel {

    public enum ListType {
        ANY("Any"), ENQUIRE("Enquire"), ACCEPT("Accept");

        public final String value;

        ListType(String value){this.value = value;}

        public static ListType getTypeFrom(String value) {
            switch (value) {
                case "Any":
                    return ListType.ANY;
                case "Enquire":
                    return ListType.ENQUIRE;
                case "Accept":
                    return ListType.ACCEPT;
                default:
                    return null;
            }
        }

        public static String[] getValues() {
            ListType[] listTypes = ListType.values();
            String[] values = new String[listTypes.length];
            for (int i=0; i<listTypes.length; i++){
                values[i] = listTypes[i].value;
            }
            return values;
        }
    }

    public ListType mListType;
    public Status mStatus;
    public boolean mDeleteMode;
    public int mSortType;
    public Query.Direction mSortDirection;
    private int mListLimit;

    public static final String[] listSortType = new String[]{"Default", "Name", "Task", "Date/Time"};

    public RequestHistoryViewModel() {
        this.reset();
    }

    public void reset(){
        mListType = ListType.ANY;
        mStatus = null;
        mDeleteMode = false;
        mSortType = 0;
        mSortDirection = Query.Direction.ASCENDING;
        mListLimit = 50;
    }

    public Query filterQuery(Query query, String uid){
        query = query.limit(mListLimit);
        if (mListType == ListType.ACCEPT){
            query = query.whereEqualTo("accepter", uid);
        }else {
            query = query.whereEqualTo("enquirer", uid);
        }
        if (mStatus != null)
            query = query.whereEqualTo("status", mStatus);
        if (mSortType > 0)
            query = query.orderBy(RequestHistoryViewModel.getFavorMemberName(mSortType), mSortDirection);
        return query;
    }

    public static String getFavorMemberName(int sortType){
        switch (sortType) {
            case 1:
                return "enquirerName";
            case 2:
                return "taskType";
            case 3:
                return "date";
            default:
                return "";
        }
    }

    public static String[] getQueryDirectionValues(){
        return new String[]{Query.Direction.ASCENDING.name(), Query.Direction.DESCENDING.name()};
    }

}
