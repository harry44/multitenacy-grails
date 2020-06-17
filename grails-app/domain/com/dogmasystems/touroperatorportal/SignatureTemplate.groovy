package com.dogmasystems.touroperatorportal

class SignatureTemplate {
    String language
    String documentType
    byte[] configFile
    String ext
    static constraints = {
         language nullable:true
         documentType nuallable :true
         configFile nullable:true
        ext nullable:true
    }
    static mapping = {
        cache true
        id generator: 'sequence', params: [sequence: 'signature_template_seq']
        version false
    }
}
