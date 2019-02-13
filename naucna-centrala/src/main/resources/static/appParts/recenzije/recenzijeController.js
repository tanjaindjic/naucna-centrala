mainModule.controller('recenzijeController', ['$http','$scope', '$window', 'mainService','PDFViewerService','$location',
       function($http, $scope, $window, mainService, pdf, $location){


       var init = function(){

           if( mainService.getSub()==""){
                mainService.goToState("login", true);
                return;
           }

           $http({
               method: 'GET',
               url: ROOT_PATH + "recenzent/" + mainService.getSub() + "/recenzije",
               headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
               console.log(result)
               $scope.recenzije = result.data;
           });
       }
       init();

        $scope.posalji = function(res){
            var payload={
                "radId":res.rad.id,
                "komentar": JSON.stringify($('#komentar' + res.id).val()),
                "rezultat": $( "#rezultat" + res.id + " option:selected" ).val()
            }
            console.log(payload)
            $http({
                   method: 'POST',
                   url: ROOT_PATH + "recenzent/" + mainService.getSub() + "/recenzije/" + res.id,
                   data: payload,
                   headers : mainService.createAuthorizationTokenHeader()
               }).then(function(result){
                   alert(result.data)
                   location.reload();
               });


        }




   }
]);