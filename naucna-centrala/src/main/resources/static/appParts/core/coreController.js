(function () {
    'use strict';
    mainModule.controller('coreController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $window, $localStorage, $location, $stateParams, mainService) {

            $scope.results = [];
            $scope.brPolja = 0;
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
                         console.log($scope.results[0])
                          $('#exampleModalLong').modal('show');

                });


            }

            $('#naprednaPretragaModal').on('hidden.bs.modal', function () {
                $("#upit0").val("");
                $('#dodatnaPolja').html('');
                $scope.brPolja = 0;
            })


            $scope.dodajPolja = function(){
                $scope.brPolja+=1;
                var container = document.createElement('div');
                container.id = "polje" + $scope.brPolja;

                $("#dodatnaPolja").append(container);
                var html =
                "<div id=\"polje" + $scope.brPolja +"\" class=\"col-md-8 text\" style=\"display:inline;\">"+
                    "<div class=\"form-group\">"+
                        "<select style=\"width:80px\" id=\"operator" + $scope.brPolja + "\" class=\"form-control choice-select\">"+
                            "<option value=\"i\">I</option>"+
                            "<option value=\"ili\">ILI</option>"+
                        "</select>"+
                    "</div>"+
                    "<div class=\"form-inline\" style=\"display:inline;\">"+
                        "<div class=\"form-group\"style=\"display:inline;\">"+
                            "<input id=\"upit" + $scope.brPolja + "\" class=\"form-control\" type=\"text\" placeholder=\"Upit\">"+
                        "</div>"+
                        "<span>   </span>"+
                        "<div class=\"form-group\" style=\"display:inline;\">"+
                            "<input id=\"checkbox" + $scope.brPolja + "\" type=\"checkbox\" value=\"\">"+
                            "<label style=\"display:inline;\">Fraza</label>"+
                            "<span>   </span>"+
                            "<button id=\"dugme" + $scope.brPolja + "\" type=\"button\" class=\"btn btn-primary\" ng-click=\"obrisi(this.id)\" style=\"display:inline;\"> Obriši</button>"+
                        "</div>"+
                    "</div>"+
                "</div>";


                $("#" + container.id).append(html);
            }

            $scope.obrisi = function(button_id){

                    console.log(button_id);
            }

            $scope.naprednaPretraga = function(){
                var upitArray = [];
                var upit0 = $('#upit0').val();
                var isFraza0 = $('#checkbox0').is(':checked');
                var upit0 = {
                    "operator": "I",
                    "upit": upit0,
                    "isFraza" : isFraza0

                }
                upitArray.push(upit0);


                var dodatnaPolja = $("#dodatnaPolja > div").length;
                console.log(dodatnaPolja);
                var i;
                if(dodatnaPolja>0){
                    for(i=1; i<=dodatnaPolja; i++){
                        var upit = {
                            "operator": $( "#operator" + i + " option:selected" ).text(),
                            "upit": $('#upit' + i).val(),
                            "isFraza": $('#checkbox' + i).is(':checked')
                        }
                        upitArray.push(upit);
                    }
                }
                console.log(upitArray)
                $http({
                        method: 'POST',
                        url: ROOT_PATH + "search/advancedQuery",
                        data: JSON.stringify(upitArray),
                        headers : mainService.createAuthorizationTokenHeader()
                    }).then(function successCallback(response) {
                        console.log(response.data)
                    }, function errorCallback(response) {
                        console.log("grerska " + JSON.stringify(response))

                    });
            }

             $scope.naCasopis = function(id){
               $('#exampleModalLong').modal('hide');
               $location.path("casopis/"+id)
            }

            $scope.naRad = function(id){
               $('#exampleModalLong').modal('hide');
               $location.path("/rad/"+id)
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