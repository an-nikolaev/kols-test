#!/usr/bin/env bash
shopt -s extglob
CURR_DATE=$(date +%Y-%m-%d_%H-%M-%S)

java -Dselenide.headless=true -Dselenide.remote=http://188.213.168.204:4444/wd/hub -DOUT=$CURR_DATE -Dselenide.chromeoptions.args="--user-agent=bot Googlebot selenium webdriver selenium-bot webdriver-bot" -Dselenide.browser="chrome" -Duser.dir=/home/ibljad/tests/jar -jar stests-1.0-SNAPSHOT.jar
mkdir ./../old/$CURR_DATE
RESULT=$(cat result.log)

MAILTO="drakonmail@gmail.com"
SUBJECT="$RESULT test report for $CURR_DATE"
BODY="./test-output/emailable-report.html"


if [ $RESULT = "Passed" ]; then
        (
         echo "To: $MAILTO"
         echo "Subject: $SUBJECT"
         echo "MIME-Version: 1.0"
         echo 'Content-Type: multipart/mixed; boundary="-q1w2e3r4t5"'
         echo
         echo '---q1w2e3r4t5'
         echo "Content-Type: text/html"
         echo "Content-Disposition: inline"
         cat $BODY
         echo '---q1w2e3r4t5'
        ) | /usr/sbin/sendmail $MAILTO
else
        ZIP_RESULTS="run_result_$CURR_DATE.zip"
        zip $ZIP_RESULTS ./test-output/emailable-report.html ./out/screenshots/$CURR_DATE/*
        (
         echo "To: $MAILTO"
         echo "Subject: $SUBJECT"
         echo "MIME-Version: 1.0"
         echo 'Content-Type: multipart/mixed; boundary="-q1w2e3r4t5"'
         echo
         echo '---q1w2e3r4t5'
         echo "Content-Type: text/html"
         echo "Content-Disposition: inline"
         cat $BODY
         echo '---q1w2e3r4t5'
         echo 'Content-Type: application/zip; name="'$(basename $ZIP_RESULTS)'"'
         echo "Content-Transfer-Encoding: base64"
         echo 'Content-Disposition: attachment; filename="'$(basename $ZIP_RESULTS)'"'
         uuencode --base64 $ZIP_RESULTS $(basename $ZIP_RESULTS)
         echo '---q1w2e3r4t5--'
        ) | /usr/sbin/sendmail $MAILTO

fi

mv ./!(stests.jar|run.sh|testng.xml|settings.properties) ./../old/$CURR_DATE