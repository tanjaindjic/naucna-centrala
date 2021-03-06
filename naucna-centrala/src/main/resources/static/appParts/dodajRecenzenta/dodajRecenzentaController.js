(function() { "use strict";

    mainModule.controller('dodajRecenzentaController', [ '$scope', '$http', '$location', '$window','$localStorage', '$stateParams','$state','mainService',
        function($scope,  $http, $location, $window, $localStorage, $stateParams, $state, mainService) {
            $scope.udaljenost = 0;
            $scope.recenzenti = [];
            $scope.rad = {};
            var init = function () {
                $scope.id = /[^/]*$/.exec(window.location.href)[0];
                $http({
                    method:"GET",
                    url:ROOT_PATH + "rad/" + $scope.id + "/recenzenti",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function(result){
                    $scope.sviRecenzenti = result.data;
                    $scope.recenzenti = result.data;
                    console.log($scope.recenzenti)
                });

                $http({
                    method:"GET",
                    url:ROOT_PATH + "rad/" + $scope.id,
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function(result){
                    $scope.rad = result.data;

                });


                console.log("init rad")

            }
            init();

            $scope.primeni = function(){
                var oblasti = [];
                $(".naucneOblasti:checkbox:checked").each(function(){
                    oblasti.push($(this).val());
                });
                if(oblasti.length<1 && $('#udaljenost').val()=="" && $('#moreLikeThis').is(':checked')==false)
                    $scope.ponisti();
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
                    console.log(response)
                    $scope.recenzenti = response.data;
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response))

                });



            }

            $scope.ponisti = function(){
                $('#udaljenost').val("");
                $('#moreLikeThis').prop('checked', false);
                $('.naucneOblasti:checkbox').prop('checked', false);
                $scope.recenzenti = $scope.sviRecenzenti;
               $scope.$apply();
            }

            $scope.posalji = function(){
                var odabrani = [];
                $(".recenzent:checkbox:checked").each(function(){
                    odabrani.push($(this).val());
                });
               $http({
                   method:"POST",
                   url:ROOT_PATH + "rad/" + $scope.id + "/naRecenziranje",
                   data: odabrani,
                   headers : mainService.createAuthorizationTokenHeader()
               }).then(function(result){
                   alert(result.data)
                   mainService.goToState("core.tasks", true);

               });
            }



        }
    ]);
})();