dim mailto
dim mailfrom
dim subject
dim body
dim attachment
mailfrom = WScript.Arguments.Item(0)
mailto = WScript.Arguments.Item(1)
subject = WScript.Arguments.Item(2)
body = WScript.Arguments.Item(3)
attachment = WScript.Arguments.Item(4)
Set emailObj      = CreateObject("CDO.Message")
emailObj.From     = "mouloud.hamdidouche@trade.gov"
emailObj.To       = "mouloud.hamdidouche@trade.gov"
emailObj.Subject  = subject
emailObj.HTMLBody = body

emailObj.AddAttachment attachment

Set emailConfig = emailObj.Configuration

emailConfig.Fields("http://schemas.microsoft.com/cdo/configuration/smtpserver") = "smtp.mysmtp.com"
emailConfig.Fields("http://schemas.microsoft.com/cdo/configuration/smtpserverport") = 465
emailConfig.Fields("http://schemas.microsoft.com/cdo/configuration/sendusing")    = 2  
emailConfig.Fields("http://schemas.microsoft.com/cdo/configuration/smtpauthenticate") = 1  
emailConfig.Fields("http://schemas.microsoft.com/cdo/configuration/smtpusessl")      = true 
emailConfig.Fields("http://schemas.microsoft.com/cdo/configuration/sendusername")    = "mouloud.hamdidouche@trade.gov"
emailConfig.Fields("http://schemas.microsoft.com/cdo/configuration/sendpassword")    = "1942@1945Dahbia"
emailConfig.Fields.Update

emailObj.Send

If err.number = 0 then Msgbox "email sent"