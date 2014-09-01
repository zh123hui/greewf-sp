package com.gree.mobile.push4wf.axisext;

import org.apache.axis.AxisFault;

public class AttachmentsImplExt extends org.apache.axis.attachments.AttachmentsImpl{

    public AttachmentsImplExt(Object intialContents, String contentType, String contentLocation) throws AxisFault {
        super(intialContents, contentType, contentLocation);
    }

}
