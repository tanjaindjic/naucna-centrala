(function () {
    "use strict";

    mainModule.service('mainService', ['$http', '$window', '$localStorage', '$state',
        function ($http, $window, $localStorage, $state) {

            this.goToState = function (state, reload) {
                $state.go(state, {
                    "sub": this.getSub()
                }, {
                    reload: reload
                });
            }

            this.getFormData = function(formFields){
                var data = [];
                formFields.forEach(element => {
                    var field = {
                        "fieldId": element.id,
                        "fieldValue": document.getElementById(element.id).value
                        }
                    data.push(field);
                });
                return data;
            }

            this.getCasopisi = function(){
                return $http({
                    method:"GET",
                    url:ROOT_PATH + "casopis",
                    headers : this.createAuthorizationTokenHeader()
                }).then(function(result){
                    return result.data;
                });
            }

            this.getCasopis = function(id){
                return $http({
                    method:"GET",
                    url:ROOT_PATH + "casopis/" + id,
                    headers : this.createAuthorizationTokenHeader()
                }).then(function(result){
                    return result.data;
                });
            }

            this.getRad = function(id){
                return $http({
                    method:"GET",
                    url:ROOT_PATH + "rad/" + id,
                    headers : this.createAuthorizationTokenHeader()
                }).then(function(result){
                    return result.data;
                });
            }

            this.basicQuery = function(query){
                return $http({
                    method:"POST",
                    url:ROOT_PATH + "search/basicQuery",
                    data: query,
                    headers : this.createAuthorizationTokenHeader()
                }).then(function(result){
                    return result.data;
                });
            }

            this.getRadovi = function(){
                var retval = [];
                $http({
                    method: 'GET',
                    url: ROOT_PATH + "rad",
                    headers : this.createAuthorizationTokenHeader()
                }).then(function(result){
                    return result.data;
                });
            }

            this.getRadoviCasopisa = function(id){
                var retval = [];
                $http({
                    method: 'GET',
                    url: ROOT_PATH + "casopis/" + id + "/radovi",
                    headers : this.createAuthorizationTokenHeader()
                }).then(function(result){
                    return result.data;
                });
            }

            this.parseJwt = function (token) {
                var base64Url = token.split('.')[1];
                var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                return JSON.parse(window.atob(base64));
            };

            this.getSub = function () {
                if (this.getJwtToken()) {
                    var token = this.parseJwt(this.getJwtToken());
                    console.log("token: " + token)
                    console.log("subject : " + token["sub"])
                    return token["sub"];
                } else return "";
            }

            this.getJwtToken = function () {
                return localStorage.getItem(JWT_TOKEN);
            }


            this.removeJwtToken = function () {
                localStorage.removeItem(JWT_TOKEN);
            }

            this.setJwtToken = function (token) {
                localStorage.setItem(JWT_TOKEN, token);
            }


            this.createAuthorizationTokenHeader = function () {
                var token = this.getJwtToken();
                if (token) {
                    return {
                        "Authorization": "Bearer " + token
                    };
                } else {
                    return {
                        "Authorization": "Bearer " + ""
                    };
                }
            }


        }
    ]);
})()