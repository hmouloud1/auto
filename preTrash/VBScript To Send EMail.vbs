'Create the objects require for sending email using CDO
Set objMail = CreateObject("CDO.Message")
Set objConf = CreateObject("CDO.Configuration")
Set objFlds = objConf.Fields

'Set various parameters and properties of CDO object
objFlds.Item("http://schemas.microsoft.com/cdo/configuration/sendusing") = 2 'cdoSendUsingPort
'your smtp server domain or IP address goes here such as smtp.yourdomain.com
objFlds.Item("http://schemas.microsoft.com/cdo/configuration/smtpserver") = "smtp.yourdomain.com" 
objFlds.Item("http://schemas.microsoft.com/cdo/configuration/smtpserverport") = 25 'default port for email
'uncomment next three lines if you need to use SMTP Authorization
'objFlds.Item("http://schemas.microsoft.com/cdo/configuration/sendusername") = "your-username"
'objFlds.Item("http://schemas.microsoft.com/cdo/configuration/sendpassword") = "your-password"
'objFlds.Item("http://schemas.microsoft.com/cdo/configuration/smtpauthenticate") = 1 'cdoBasic
objFlds.Update
objMail.Configuration = objConf
objMail.From = "mouloud.hamdidouche@trade.gov"
objMail.To = "mouloud.hamdidouche@trade.gov"
objMail.Subject = "Put your email's subject line here"
objMail.TextBody = "Your email body content goes here"
objMail.Send

'Set all objects to nothing after sending the email
Set objFlds = Nothing
Set objConf = Nothing
Set objMail = Nothing