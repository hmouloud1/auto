Start "segment_api__1" java -Xmx1024m -jar segment_api.jar > test.log
wait 1000
Start "segment_api__2" java -Xmx1024m -jar segment_api.jar > test2.log
pause