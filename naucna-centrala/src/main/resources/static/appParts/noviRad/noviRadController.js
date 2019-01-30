(function () {
    'use strict';
    mainModule.controller('noviRadController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {

            $scope.selectedCar = {};
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
             console.log(document.getElementById("selectedCar").options)
                console.log(document.getElementById("selectedCar").options.selectedIndex)
                console.log($scope.casopisi[document.getElementById("selectedCar").options.selectedIndex-1])
            }



        }
    ]);
})();