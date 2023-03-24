'TWO FUNCTIONS
    'SAME EXCEPT FIRST TAKES A STRING FOR ATTACHMENT
    'SECOND TAKES AN ARRAY LIST SO YOU CAN SEND MULTIPLE 
         'ATTACHMENTS
    'FROM: Email address FRom
    'TO: EMAIL address To
    'Subject: Subject; Body: MessageText
    'Optional CC, BCC: CC and bcc recipients
    'SMTPSERVER: Optional, if not specified 
   'local machine is used
    'AttachmentFile (first function: Optional, file name)
    'AttachmentFiles (second function: Optional, list of     
        'attachments in form of an array list)
	call SendMailOneAttachment ("mouloud.hamdidouche@trade.gov", 
	"mouloud.hamdidouche@trade.gov",
"subject test", "body")
    Public Sub SendMailOneAttachment(ByVal From As String, _
      ByVal sendTo As String, ByVal Subject As String, _
      ByVal Body As String, _
      Optional ByVal AttachmentFile As String = "", _
      Optional ByVal CC As String = "", _
      Optional ByVal BCC As String = "", _
      Optional ByVal SMTPServer As String = "")

        Dim myMessage As MailMessage

        Try
            myMessage = New MailMessage()
            With myMessage
                .To = sendTo
                .From = From
                .Subject = Subject
                .Body = Body
                .BodyFormat = MailFormat.Text
                'CAN USER MAILFORMAT.HTML if you prefer

                If CC <> "" Then .Cc = CC
                If BCC <> "" Then .Bcc = ""

                If FileExists(AttachmentFile) Then _
                 .Attachments.Add(AttachmentFile)

            End With

            If SMTPServer <> "" Then _
               SmtpMail.SmtpServer = SMTPServer
            SmtpMail.Send(myMessage)

        Catch myexp As Exception
            Throw myexp
        End Try

    End Sub

Public Sub SendMailMultipleAttachments(ByVal From As String,_
    ByVal sendTo As String, ByVal Subject As String, _
    ByVal Body As String, _
    Optional ByVal AttachmentFiles As ArrayList = Nothing, _
    Optional ByVal CC As String = "", _
    Optional ByVal BCC As String = "", _
    Optional ByVal SMTPServer As String = "")

        Dim myMessage As MailMessage
        Dim i, iCnt As Integer

        Try
            myMessage = New MailMessage()
            With myMessage
                .To = sendTo
                .From = From
                .Subject = Subject
                .Body = Body
                .BodyFormat = MailFormat.Text
                'CAN USER MAILFORMAT.HTML if you prefer

                If CC <> "" Then .Cc = CC
                If BCC <> "" Then .Bcc = ""

                If Not AttachmentFiles Is Nothing Then
                    iCnt = AttachmentFiles.Count - 1
                    For i = 0 To iCnt
                        If FileExists(AttachmentFiles(i)) Then _
                          .Attachments.Add(AttachmentFiles(i))
                    Next

                End If

            End With

            If SMTPServer <> "" Then _
              SmtpMail.SmtpServer = SMTPServer
            SmtpMail.Send(myMessage)


        Catch myexp As Exception
            Throw myexp
        End Try
    End Sub

    Private Function FileExists(ByVal FileFullPath As String) _
     As Boolean
        If Trim(FileFullPath) = "" Then Return False

        Dim f As New IO.FileInfo(FileFullPath)
        Return f.Exists

    End Function