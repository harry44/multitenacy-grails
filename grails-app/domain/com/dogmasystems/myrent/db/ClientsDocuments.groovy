package com.dogmasystems.myrent.db

class ClientsDocuments implements Serializable
{


    MRBusinessPartner clients
    String dlNumber;
    String dlName;
    String dlIssueDate;
    String dlFront;
    String dlBack;
    String passportFront;
    String passportBack;
    String documentOne;
    String documentTwo;
    String documentThree;
    String profilePic;

    static mapping = {
        cache true
        id generator: 'sequence', params:[sequence:'clientsdocuments_seq']
        id column: "id", sqlType: 'int4'
        clients column:"id_clienti", sqlType: 'int4'

        dlFront column:"dl_front"
        dlBack column:"dl_back"

        dlNumber column:"dl_number"
        dlName column:"dl_name"
        dlIssueDate column:"dl_issue_date"
        passportFront column:"passport_front"
        passportBack column:"passport_back"
        documentOne column:"document_one"
        documentTwo column:"document_two"
        documentThree column:"document_three"
        profilePic column:"profile_pic"
        version false
    }

    static constraints = {
        dlNumber  nullable:true
        dlIssueDate nullable:true
        dlName nullable:true
        dlFront  nullable:true
        dlBack  nullable:true
        passportFront  nullable:true
        passportBack  nullable:true
        documentOne  nullable:true
        documentTwo  nullable:true
        documentThree  nullable:true
        profilePic  nullable:true
    }
}
