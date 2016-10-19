angular.module('app').controller("controller", function($http, $scope, $modal) {
	$scope.user = {};
	$scope.loggedUser = {};
	$scope.logged = false;
	$scope.init = function() {
		$http.get('rest/getUser').success(function(result) {
			if (result != "null") {
				$scope.logged = true;
				$scope.user = result;
				$scope.loggedUser = result;
			}
		});
	};

	$scope.login = function() {
		var data = {};
		data.login = $scope.user.login;
		data.password = $scope.user.password;
		$http.get('rest/login', {
			params : data
		}).success(function(result) {
			$scope.logged = result != "";
			$scope.user = result;
			$scope.loggedUser = $scope.user;
			if (!$scope.logged) {
				alert("Nieprawidłowe dane logowania");
			}
		}).error(function(error, status) {
			alert(error);
		});
	}

	$scope.logout = function() {
		$scope.logged = false;
		$scope.user = {};
		$scope.loggedUser = {};
		$http.get('rest/logout');
	}

	$scope.register = function(movie) {
		var modalInstance = $modal.open({
			templateUrl : 'register.html',
			controller : 'registerController',
			size : 'lg'
		});
	};

	$scope.doRegister = function(form) {
		$http.get({
			url : "rest/register",
			params : {
				"name" : form.name,
				"login" : form.login,
				"password" : form.password
			}
		}).success(function(data) {
			$modalInstance.close();

		}).error(function(error, status) {
			alert(status);
		});
	}
});