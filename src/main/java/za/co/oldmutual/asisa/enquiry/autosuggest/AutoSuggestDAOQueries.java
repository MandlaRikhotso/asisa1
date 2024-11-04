package za.co.oldmutual.asisa.enquiry.autosuggest;

public class AutoSuggestDAOQueries {

  private AutoSuggestDAOQueries() {

  }

  public static final String FETCH_IDENTITY_NUMBER_QUERY =
      "SELECT IDENTITY_NUMBER FROM LC_INSURED_PERSON "
          + "WHERE IDENTITY_NUMBER LIKE :identityNumber";

  public static final String FETCH_SURNAME_QUERY = "SELECT SURNAME FROM LC_INSURED_PERSON "
      + "WHERE UPPER(LC_INSURED_PERSON.SURNAME) LIKE UPPER(:surname)";
}
