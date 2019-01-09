(function() { "use strict";

    mainModule.controller('loginController', [ '$scope', '$http', '$window','$localStorage','$location', '$stateParams',
        function($scope,  $http, $location, $window, $localStorage, $stateParams) {
            

            $scope.init = function(){
                $scope.username="";
                $scope.pass="";
            }

          
            $scope.login = function(){
                var payload = {
                    "username" : $scope.username,
                    "pass" : $scope.pass
                }
                $http({
                    method: 'POST',
                    url:  ROOT_PATH + "korisnik/login",
                    data: payload
                    }).then(function successCallback(response) {
                        console.log("Uspeh: " + JSON.stringify(response.data))
                        window.localStorage.setItem('token', response.data.token)
                        console.log("Postavljen: " + window.localStorage.getItem('token'))
                       // $location.path(response.data.Location);
                    }, function errorCallback(response) {
                         console.log("grerska " + JSON.stringify(response.data))
                        
                });
    
            }
        }
    ]);
})();