(function () {
    'use strict';
    mainModule.controller('noviRadController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {

            $scope.selectedCar = {};
            $scope.firstForm = true;
            $scope.oblasti = [];
            $scope.myFile = {};
            var init = function () {

                console.log("Usao u init novog rada");
                 $http({
                    method: 'GET',
                    url: ROOT_PATH + "casopis",
                    headers : mainService.createAuthorizationTokenHeader()
                 }).then(function successCallback(response) {
                    console.log("Response:")
                    console.log(JSON.stringify(response));
                    $scope.casopisi = response.data;
                 }, function errorCallback(response) {
                     console.log("grerska " + JSON.stringify(response))

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


            $scope.slanje = function(){
                var file = $scope.myFile;
                var fd = new FormData();
                fd.append('file', file);
                var auth = mainService.createAuthorizationTokenHeader();
                var uploadUrl = ROOT_PATH + "rad";
                $http.post(uploadUrl, fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined, auth}
                }).then(function successCallback(response) {
                    console.log(JSON.stringify(response.data));
                }, function errorCallback(response) {
                    alert("fail :(")
                    console.log(JSON.stringify(response.data))

                });

               /* var formData = mainService.getFormData($scope.formFields);
                var payload = {
                    "taskId" : window.localStorage.getItem('taskId'),
                    "processInstanceId" : window.localStorage.getItem('processInstanceId'),
                    "formFields" : formData
                }
                console.log(JSON.stringify(payload))
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "rad",
                    data: JSON.stringify(payload)
                }).then(function successCallback(response) {
                   alert("Succ")
                }, function errorCallback(response) {
                    alert("fail :(")
                    console.log(JSON.stringify(response.data))

                });*/
            }

        }
    ]);
})();