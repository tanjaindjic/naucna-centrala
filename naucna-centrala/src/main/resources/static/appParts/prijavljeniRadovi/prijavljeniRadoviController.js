(function () {
    "use strict";

    mainModule.controller('prijavljeniRadoviController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {


            var init = function () {

               $http({
                   method: 'GET',
                   url: ROOT_PATH + "korisnik/" + mainService.getSub() + "/recenziraniRadovi",
                   headers : mainService.createAuthorizationTokenHeader()
               }).then(function(result){
                   console.log(result)
                   $scope.naDoradi = result.data;
               });

            }
            init();

            $scope.slanje = function(id){
                console.log("usao u slanje, id: " + id)
               var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
               var files = singleFileUploadInput.files;
               var fileAddress = "";
               if(files.length === 0) {
                   alert("Morate dodati novu verziju rada za slanje.")
               }
               var file = files[0];
               var formData = new FormData();
               formData.append("file", file);
               formData.append("odgovor", $("#odgovor").val());
               console.log("saljem novu verziju: " + JSON.stringify(formData))
               var xhr = new XMLHttpRequest();

               xhr.open("POST", "/rad/"+id+"/novaVerzija");
               xhr.setRequestHeader('Access-Control-Allow-Headers', 'Content-Type');
               xhr.send(formData);
               xhr.onload=function() {
                   alert(this.response);
                   location.reload();
               };
            }

            $scope.objavljeno = function(){
                document.getElementById("prikaziObjavljeno").style.display="block";
                document.getElementById("prikaziNaDoradi").style.display="none";
           }
           $scope.recenzije = function(){
                document.getElementById("prikaziObjavljeno").style.display="none";
                document.getElementById("prikaziNaDoradi").style.display="block";
           }

        }
    ]);
})();