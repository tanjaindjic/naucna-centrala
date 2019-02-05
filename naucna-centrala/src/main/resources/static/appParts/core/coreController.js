(function () {
    'use strict';
    mainModule.controller('coreController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {

            $scope.results = [];
            var init = function () {
                 console.log("init core")
                if (mainService.getJwtToken()) {
                    document.getElementById("prijava").style.display = "none";
                    document.getElementById("odjava").style.display = "block";
                } else {
                    document.getElementById("prijava").style.display = "block";
                    document.getElementById("odjava").style.display = "none";
                }
            }
            init();

            $scope.odjava = function () {
                mainService.removeJwtToken();
                $http({
                    method: 'GET',
                    url: ROOT_PATH + "korisnik/logout"
                })
                mainService.goToState("core.home", true);
            }

            $scope.prijava = function () {
                mainService.goToState("login", true);
            }

            $scope.trazi = function(){
                var zaPretragu = document.getElementById("zaPretragu").value;
                console.log("uneo: " + zaPretragu)
                var myDataPromise = mainService.basicQuery(zaPretragu);
                    myDataPromise.then(function(result) {
                         console.log(result)
                         $scope.results = result;
                         console.log($scope.results[0].id)
                          $('#exampleModalLong').modal('show');

                });


            }

            $scope.novirad = function(){
 
                $http({
                    method: 'GET',
                    url: ROOT_PATH + "rad/prijavaRada",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function successCallback(response) {
                    console.log("Response:")
                    console.log(JSON.stringify(response));
                    var obj = response.data;
                    window.localStorage.setItem('taskId', obj["taskId"]);
                    window.localStorage.setItem('processInstanceId', obj["processInstanceId"]);
                    mainService.goToState(obj["location"], true);
                }, function errorCallback(response) {
                     console.log("grerska " + JSON.stringify(response))

                 });
            }
        }
    ]);
})();