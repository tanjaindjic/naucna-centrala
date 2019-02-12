(function () {
    'use strict';
    mainModule.controller('noviRadController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {

            $scope.selectedCar = {};
            $scope.firstForm = true;
            $scope.secondForm = false;
            $scope.uploadForm = false;
            $scope.oblasti = [];
            $scope.myFile = {};
            $scope.formFields = [];
            $scope.casopisi = [];

            var init = function () {

                console.log("Usao u init novog rada");
                var myDataPromise = mainService.getCasopisi();
                    myDataPromise.then(function(result) {
                        $scope.casopisi = result;
                        console.log($scope.casopisi);
                });

                $http({
                    method: 'GET',
                    url: ROOT_PATH + "rad/odabirCasopisa/" + window.localStorage.getItem('taskId'),
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function successCallback(response) {
                    console.log("Response:")
                    console.log(JSON.stringify(response));
                    var obj = response.data;
                    $scope.formFields = obj["formField"];
                    console.log($scope.formFields.length)
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response))

                });
            }
            init();

            $scope.dalje = function(){

                $http({
                    method: 'GET',
                    url: ROOT_PATH + "casopis/naucneOblasti/",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function successCallback(response) {
                    console.log("Response:")
                    console.log(JSON.stringify(response));
                    $scope.oblasti = response.data;
                    console.log($scope.oblasti)
                });
                $scope.naucnaOblast = $scope.oblasti[0];

                $scope.firstForm = false;
                $scope.secondForm = true;

                console.log($scope.casopisi[document.getElementById("selectedCar").options.selectedIndex-1])
                var formData = [];
                var field = {
                    "fieldId": "odabraniCasopis",
                    "fieldValue": $scope.casopisi[document.getElementById("selectedCar").options.selectedIndex-1].id
                }
                formData.push(field);

                var payload = {
                    "taskId" : window.localStorage.getItem('taskId'),
                    "processInstanceId" : window.localStorage.getItem('processInstanceId'),
                    "formFields" : formData
                }
                console.log(JSON.stringify(payload))
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "rad/odabirCasopisa",
                    data: JSON.stringify(payload)
                }).then(function successCallback(response) {
                    console.log(JSON.stringify(response.data));
                    var obj = response.data;
                    $scope.formFields = obj["formField"];
                    $scope.formFields.pop();
                    console.log(obj["taskId"])
                    console.log(obj["processInstanceId"])
                    console.log($scope.formFields)
                    window.localStorage.setItem('taskId', obj["taskId"]);
                    window.localStorage.setItem('processInstanceId', obj["processInstanceId"]);

                }, function errorCallback(response) {
                    alert("fail :(")
                    console.log(JSON.stringify(response.data))

                });


            }

            $scope.dalje2 = function(){
                $scope.formData = [];
                var data1={};
                var data2={};
                var data3={};
                var data4={};
                var data5={};
                data1["fieldId"] = "naslov";
                data1["fieldValue"] = document.getElementById("naslov").value;
                $scope.formData.push(data1);
                data2["fieldId"] = "koautori";
                data2["fieldValue"] = document.getElementById("koautori").value;
                $scope.formData.push(data2);
                data3["fieldId"] = "kljucniPojmovi";
                data3["fieldValue"] = document.getElementById("kljucniPojmovi").value;
                $scope.formData.push(data3);
                data4["fieldId"] = "apstrakt";
                data4["fieldValue"] = document.getElementById("apstrakt").value;
                $scope.formData.push(data4);
                data5["fieldId"] = "naucnaOblast";
                data5["fieldValue"] = $scope.oblasti[document.getElementById("naucnaOblast").options.selectedIndex-1];
                $scope.formData.push(data5);


                $scope.secondForm = false;
                $scope.uploadForm = true;

            }

            $scope.slanje = function(){
                var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
                var files = singleFileUploadInput.files;
                var fileAddress = "";
                if(files.length === 0) {
                    alert("select a file!")
                }
                var file = files[0];
                var formData = new FormData();
                formData.append("file", file);

                var xhr = new XMLHttpRequest();
                xhr.open("POST", "/rad/nacrt");

                xhr.onload = function() {
                    console.log(fileAddress)
                    if(xhr.status == 200) {
                        alert("succc")
                        var data={};
                        data["fieldId"] = "rad";
                        data["fieldValue"] = xhr.responseText;
                        $scope.formData.push(data);

                        var payload = {
                           "taskId" : window.localStorage.getItem('taskId'),
                           "processInstanceId" : window.localStorage.getItem('processInstanceId'),
                           "formFields" : $scope.formData
                        }
                        console.log(JSON.stringify(payload))
                        $http({
                           method: 'POST',
                           url: ROOT_PATH + "rad/create",
                           data: JSON.stringify(payload)
                        }).then(function successCallback(response) {
                            alert("BRAVO JA")

                        }, function errorCallback(response) {
                           alert("fail :(")

                        });
                    } else {
                        alert("fail")
                    }
                }

                xhr.send(formData);


            }



        }
    ]);
})();