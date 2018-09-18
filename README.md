# ho2-mojo-model-scorer
H2O's Mojo Model REST web service for real-time scoring

You could publish your XGBoost and Stacked Ensembles H2O's mojo models as a REST API  
Download from https://github.com/rubuntu/ho2-mojo-model-scorer/raw/master/mojo_scorer/target/mojo_scorer.war

### Inspired by
* https://github.com/openscoring/openscoring
* https://github.com/h2oai/app-consumer-loan

### Scripts for generate sample models
Based on H2O's app-consumer-loan sample
* R  
  https://raw.githubusercontent.com/rubuntu/ho2-mojo-model-scorer/master/mojo_scorer/script.Rmd

* Python  
  https://raw.githubusercontent.com/rubuntu/ho2-mojo-model-scorer/master/mojo_scorer/script.py

## REST API 

### Model REST API endpoints:

| HTTP method | Endpoint | Required role(s) | Description |
| ----------- | -------- | ---------------- | ----------- |
| GET | /model/${id} | - | Get the summary of a model |
| POST | /model/${id} | - | Evaluate data in "single prediction" mode |

### Mojo admin REST API endpoints (webdav) :

| HTTP method | Endpoint | Required role(s) | Description |
| ----------- | -------- | ---------------- | ----------- |
| GET | /mojos | admin | List all Mojo files |
| PUT | /mojos/${mojo_id} | admin | Deploy a model |
| DELETE | /mojos/${mojo_id} | admin | Undeploy a model |
| GET | /mojos/${mojo_id} | admin | Download a model as a Mojo file |


### Test with Node.js 
Sample in https://github.com/rubuntu/ho2-mojo-model-scorer/tree/master/mojo_scorer/src/test/node
Using endpoint "http://localhost:8080/mojo_scorer/model/InterestRateModel for Mojo model file: InterestRateModel.zip

```
{ node }  » npm update
{ node }  » head eval.csv                                                                                              ~/desktop/mojo_scorer/src/test/node
"id","loan_amnt","longest_credit_length","revol_util","emp_length","home_ownership","annual_inc","purpose","addr_state","dti","delinq_2yrs","total_acc","verification_status","term","bad_loan","int_rate"
"1",3000,4,87.5,9,"RENT",48000,"car","CA",5.35,0,4,"verified","36 months","0",18.64
"2",6000,8,37.73,1,"MORTGAGE",84000,"medical","UT",18.44,2,14,"verified","36 months","0",11.71
"3",15000,9,93.9,2,"MORTGAGE",92000,"credit_card","IL",29.44,0,31,"verified","36 months","0",9.91
"4",5000,6,29.3,2,"RENT",24044,"debt_consolidation","OR",11.93,0,16,"verified","36 months","0",8.9
"5",12000,17,21,10,"RENT",62300,"debt_consolidation","NJ",16.7,0,25,"not verified","36 months","0",7.9
"6",4400,7,99,10,"RENT",55000,"debt_consolidation","RI",20.01,0,11,"not verified","36 months","0",16.77
"7",12000,11,52.1,1,"RENT",46000,"credit_card","TX",8.11,0,12,"not verified","36 months","0",9.91
"8",21000,13,97.6,7,"RENT",50000,"debt_consolidation","WA",21.58,0,14,"verified","60 months","1",19.91
"9",10000,11,59.1,2,"RENT",51400,"credit_card","TX",19.14,0,24,"not verified","36 months","0",10.65
{ node }  » node test.js   
2018-09-17 20:47:50  data: { emp_length: '9',
  annual_inc: '48000',
  purpose: 'car',
  bad_loan: '0',
  home_ownership: 'RENT',
  verification_status: 'verified',
  revol_util: '87.5',
  addr_state: 'CA',
  dti: '5.35',
  delinq_2yrs: '0',
  loan_amnt: '3000',
  total_acc: '4',
  term: '36 months',
  id: '1',
  int_rate: '18.64',
  longest_credit_length: '4',
  model_name: 'InterestRateModel',
  score: 15.468917248986411,
  error_msg: '' }
2018-09-17 20:47:50  data: { emp_length: '1',
  annual_inc: '84000',
  purpose: 'medical',
  bad_loan: '0',
  home_ownership: 'MORTGAGE',
  verification_status: 'verified',
  revol_util: '37.73',
  addr_state: 'UT',
  dti: '18.44',
  delinq_2yrs: '2',
  loan_amnt: '6000',
  total_acc: '14',
  term: '36 months',
  id: '2',
  int_rate: '11.71',
  longest_credit_length: '8',
  model_name: 'InterestRateModel',
  score: 15.501789328790704,
  error_msg: '' }

{ node }  » head eval-scored.csv                                                                                       ~/desktop/mojo_scorer/src/test/node
"id","score","error_msg"
"1","15.468917248986411",""
"2","15.501789328790704",""
"3","14.144379968554215",""
"4","12.243136631045939",""
"5","10.050904153916502",""
"6","15.444714245938751",""
"7","12.121465678076941",""
"8","20.230435580454735",""
"9","12.528044484278592",""

```
### Performance
```
$ ab -k -c 8 -n 10000 "10.9.100.95:8080/mojo_scorer/model/BadLoanModel?id=1&loan_amnt=10000&term=36+months&emp_length=5&home_ownership=RENT&annual_inc=60000&verification_status=verified&income&purpose=debt_consolidation&addr_state=FL&dti=3&delinq_2yrs=0&revol_util=35&total_acc=4&longest_credit_length=10"
This is ApacheBench, Version 2.3 <$Revision: 1706008 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 10.9.100.95 (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:        Apache-Coyote/1.1
Server Hostname:        10.9.100.95
Server Port:            8080

Document Path:          /mojo_scorer/model/BadLoanModel?id=1&loan_amnt=10000&term=36+months&emp_length=5&home_ownership=RENT&annual_inc=60000&verification_status=verified&income&purpose=debt_consolidation&addr_state=FL&dti=3&delinq_2yrs=0&revol_util=35&total_acc=4&longest_credit_length=10
Document Length:        375 bytes

Concurrency Level:      8
Time taken for tests:   2.552 seconds
Complete requests:      10000
Failed requests:        0
Keep-Alive requests:    9904
Total transferred:      5349520 bytes
HTML transferred:       3750000 bytes
Requests per second:    3918.45 [#/sec] (mean)
Time per request:       2.042 [ms] (mean)
Time per request:       0.255 [ms] (mean, across all concurrent requests)
Transfer rate:          2047.06 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       9
Processing:     1    2   1.1      2      17
Waiting:        0    2   1.1      2      17
Total:          1    2   1.1      2      17

Percentage of the requests served within a certain time (ms)
  50%      2
  66%      2
  75%      2
  80%      2
  90%      2
  95%      3
  98%      5
  99%      8
 100%     17 (longest request)

```
On PC with a 3.4 GHz Intel Core i7 3rd generation this run gives:
throughput of 3918 requests / second
