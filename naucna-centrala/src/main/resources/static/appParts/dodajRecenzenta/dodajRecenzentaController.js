(function() { "use strict";

    mainModule.controller('dodajRecenzentaController', [ '$scope', '$http', '$location', '$window','$localStorage', '$stateParams','$state','mainService',
        function($scope,  $http, $location, $window, $localStorage, $stateParams, $state, mainService) {
            $scope.udaljenost = 0;
            $scope.recenzenti = [];
            $scope.rad = {};
            var init = function () {
                $scope.id = /[^/]*$/.exec(window.location.href)[0];
                return $http({
                    method:"GET",
                    url:ROOT_PATH + "rad/" + $scope.id + "/recenzenti",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function(result){
                    $scope.sviRecenzenti = result.data;
                    $scope.recenzenti = result.data;
                    console.log($scope.recenzenti)
                });

                console.log("init rad")

            }
            init();

            $scope.primeni = function(){
                var oblasti = [];
                $(".naucneOblasti:checkbox:checked").each(function(){
                    oblasti.push($(this).val());
                });
                var payload = {
                    "idRada":$scope.id,
                    "udaljenost":$('#udaljenost').val(),
                    "moreLikeThis":$('#moreLikeThis').is(':checked'),
                    "naucneOblasti":oblasti
                }
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "search/recenzentQuery",
                    data: JSON.stringify(payload),
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function successCallback(response) {
                    $scope.recenzenti = response.data;
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response))

                });



            }

            $scope.ponisti = function(){
                $('#udaljenost').val("");
                $('#moreLikeThis').prop('checked', false);
                $('.naucneOblasti:checkbox').prop('checked', true);
                $scope.recenzenti = $scope.sviRecenzenti;
               $scope.$apply();
            }



        }
    ]);
})();