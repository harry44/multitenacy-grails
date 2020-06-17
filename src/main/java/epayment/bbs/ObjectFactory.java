
package epayment.bbs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.datacontract.schemas._2004._07.bbs_epayment.ArrayOfProcessRequest;
import org.datacontract.schemas._2004._07.bbs_epayment.ArrayOfProcessResponse;
import org.datacontract.schemas._2004._07.bbs_epayment.ProcessRequest;
import org.datacontract.schemas._2004._07.bbs_epayment.QueryRequest;
import org.datacontract.schemas._2004._07.bbs_epayment.ReconRequest;
import org.datacontract.schemas._2004._07.bbs_epayment.RegisterRequest;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the epayment.bbs package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RegisterMerchantId_QNAME = new QName("http://BBS.EPayment", "merchantId");
    private final static QName _RegisterToken_QNAME = new QName("http://BBS.EPayment", "token");
    private final static QName _RegisterRequest_QNAME = new QName("http://BBS.EPayment", "request");
    private final static QName _RegisterResponseRegisterResult_QNAME = new QName("http://BBS.EPayment", "RegisterResult");
    private final static QName _QueryResponseQueryResult_QNAME = new QName("http://BBS.EPayment", "QueryResult");
    private final static QName _ProcessResponseProcessResult_QNAME = new QName("http://BBS.EPayment", "ProcessResult");
    private final static QName _BatchRequests_QNAME = new QName("http://BBS.EPayment", "requests");
    private final static QName _BatchResponseBatchResult_QNAME = new QName("http://BBS.EPayment", "BatchResult");
    private final static QName _ReconResponseReconResult_QNAME = new QName("http://BBS.EPayment", "ReconResult");
    private final static QName _CheckAvailabilityResponseCheckAvailabilityResult_QNAME = new QName("http://BBS.EPayment", "CheckAvailabilityResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: epayment.bbs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Register }
     * 
     */
    public Register createRegister() {
        return new Register();
    }

    /**
     * Create an instance of {@link epayment.bbs.RegisterResponse }
     * 
     */
    public epayment.bbs.RegisterResponse createRegisterResponse() {
        return new epayment.bbs.RegisterResponse();
    }

    /**
     * Create an instance of {@link Query }
     * 
     */
    public Query createQuery() {
        return new Query();
    }

    /**
     * Create an instance of {@link epayment.bbs.QueryResponse }
     * 
     */
    public epayment.bbs.QueryResponse createQueryResponse() {
        return new epayment.bbs.QueryResponse();
    }

    /**
     * Create an instance of {@link Process }
     * 
     */
    public Process createProcess() {
        return new Process();
    }

    /**
     * Create an instance of {@link epayment.bbs.ProcessResponse }
     * 
     */
    public epayment.bbs.ProcessResponse createProcessResponse() {
        return new epayment.bbs.ProcessResponse();
    }

    /**
     * Create an instance of {@link Batch }
     * 
     */
    public Batch createBatch() {
        return new Batch();
    }

    /**
     * Create an instance of {@link BatchResponse }
     * 
     */
    public BatchResponse createBatchResponse() {
        return new BatchResponse();
    }

    /**
     * Create an instance of {@link Recon }
     * 
     */
    public Recon createRecon() {
        return new Recon();
    }

    /**
     * Create an instance of {@link epayment.bbs.ReconResponse }
     * 
     */
    public epayment.bbs.ReconResponse createReconResponse() {
        return new epayment.bbs.ReconResponse();
    }

    /**
     * Create an instance of {@link CheckAvailability }
     * 
     */
    public CheckAvailability createCheckAvailability() {
        return new CheckAvailability();
    }

    /**
     * Create an instance of {@link CheckAvailabilityResponse }
     * 
     */
    public CheckAvailabilityResponse createCheckAvailabilityResponse() {
        return new CheckAvailabilityResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "merchantId", scope = Register.class)
    public JAXBElement<String> createRegisterMerchantId(String value) {
        return new JAXBElement<String>(_RegisterMerchantId_QNAME, String.class, Register.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "token", scope = Register.class)
    public JAXBElement<String> createRegisterToken(String value) {
        return new JAXBElement<String>(_RegisterToken_QNAME, String.class, Register.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "request", scope = Register.class)
    public JAXBElement<RegisterRequest> createRegisterRequest(RegisterRequest value) {
        return new JAXBElement<RegisterRequest>(_RegisterRequest_QNAME, RegisterRequest.class, Register.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.bbs_epayment.RegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "RegisterResult", scope = epayment.bbs.RegisterResponse.class)
    public JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.RegisterResponse> createRegisterResponseRegisterResult(org.datacontract.schemas._2004._07.bbs_epayment.RegisterResponse value) {
        return new JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.RegisterResponse>(_RegisterResponseRegisterResult_QNAME, org.datacontract.schemas._2004._07.bbs_epayment.RegisterResponse.class, epayment.bbs.RegisterResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "merchantId", scope = Query.class)
    public JAXBElement<String> createQueryMerchantId(String value) {
        return new JAXBElement<String>(_RegisterMerchantId_QNAME, String.class, Query.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "token", scope = Query.class)
    public JAXBElement<String> createQueryToken(String value) {
        return new JAXBElement<String>(_RegisterToken_QNAME, String.class, Query.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "request", scope = Query.class)
    public JAXBElement<QueryRequest> createQueryRequest(QueryRequest value) {
        return new JAXBElement<QueryRequest>(_RegisterRequest_QNAME, QueryRequest.class, Query.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.bbs_epayment.QueryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "QueryResult", scope = epayment.bbs.QueryResponse.class)
    public JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.QueryResponse> createQueryResponseQueryResult(org.datacontract.schemas._2004._07.bbs_epayment.QueryResponse value) {
        return new JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.QueryResponse>(_QueryResponseQueryResult_QNAME, org.datacontract.schemas._2004._07.bbs_epayment.QueryResponse.class, epayment.bbs.QueryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "merchantId", scope = Process.class)
    public JAXBElement<String> createProcessMerchantId(String value) {
        return new JAXBElement<String>(_RegisterMerchantId_QNAME, String.class, Process.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "token", scope = Process.class)
    public JAXBElement<String> createProcessToken(String value) {
        return new JAXBElement<String>(_RegisterToken_QNAME, String.class, Process.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "request", scope = Process.class)
    public JAXBElement<ProcessRequest> createProcessRequest(ProcessRequest value) {
        return new JAXBElement<ProcessRequest>(_RegisterRequest_QNAME, ProcessRequest.class, Process.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "ProcessResult", scope = epayment.bbs.ProcessResponse.class)
    public JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse> createProcessResponseProcessResult(org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse value) {
        return new JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse>(_ProcessResponseProcessResult_QNAME, org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse.class, epayment.bbs.ProcessResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "merchantId", scope = Batch.class)
    public JAXBElement<String> createBatchMerchantId(String value) {
        return new JAXBElement<String>(_RegisterMerchantId_QNAME, String.class, Batch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "token", scope = Batch.class)
    public JAXBElement<String> createBatchToken(String value) {
        return new JAXBElement<String>(_RegisterToken_QNAME, String.class, Batch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProcessRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "requests", scope = Batch.class)
    public JAXBElement<ArrayOfProcessRequest> createBatchRequests(ArrayOfProcessRequest value) {
        return new JAXBElement<ArrayOfProcessRequest>(_BatchRequests_QNAME, ArrayOfProcessRequest.class, Batch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProcessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "BatchResult", scope = BatchResponse.class)
    public JAXBElement<ArrayOfProcessResponse> createBatchResponseBatchResult(ArrayOfProcessResponse value) {
        return new JAXBElement<ArrayOfProcessResponse>(_BatchResponseBatchResult_QNAME, ArrayOfProcessResponse.class, BatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "merchantId", scope = Recon.class)
    public JAXBElement<String> createReconMerchantId(String value) {
        return new JAXBElement<String>(_RegisterMerchantId_QNAME, String.class, Recon.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "token", scope = Recon.class)
    public JAXBElement<String> createReconToken(String value) {
        return new JAXBElement<String>(_RegisterToken_QNAME, String.class, Recon.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReconRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "request", scope = Recon.class)
    public JAXBElement<ReconRequest> createReconRequest(ReconRequest value) {
        return new JAXBElement<ReconRequest>(_RegisterRequest_QNAME, ReconRequest.class, Recon.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.bbs_epayment.ReconResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "ReconResult", scope = epayment.bbs.ReconResponse.class)
    public JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.ReconResponse> createReconResponseReconResult(org.datacontract.schemas._2004._07.bbs_epayment.ReconResponse value) {
        return new JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.ReconResponse>(_ReconResponseReconResult_QNAME, org.datacontract.schemas._2004._07.bbs_epayment.ReconResponse.class, epayment.bbs.ReconResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://BBS.EPayment", name = "CheckAvailabilityResult", scope = CheckAvailabilityResponse.class)
    public JAXBElement<String> createCheckAvailabilityResponseCheckAvailabilityResult(String value) {
        return new JAXBElement<String>(_CheckAvailabilityResponseCheckAvailabilityResult_QNAME, String.class, CheckAvailabilityResponse.class, value);
    }

}
