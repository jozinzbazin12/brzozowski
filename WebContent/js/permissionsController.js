angular.module('app').controller('permissionsController', function($http, $scope, items, $modal, $modalInstance) {
	$scope.note = items[0];
	$scope.user = items[1];

	$scope.init = function() {
		$http.get("rest/getUsers").success(function(data) {
			$scope.users = data;
			$scope.users = $scope.users.filter(function(u) {
				return u.login !== $scope.user.login;
			});
		}).error(function(error, status) {
			alert(error);
		});
	}

	$scope.init();

	$scope.hasPermissions = function(user) {
		var result = $scope.note.editors.filter(function(e) {
			return e.login === user.login;
		});
		return result.length > 0;
	}

	$scope.add = function(user) {
		$http.get("rest/addUser", {
			params : {
				"messageId" : $scope.note.messageId,
				"user" : user.login
			}
		}).success(function(data) {
			$scope.note.editors.push(user);
		}).error(function(error, status) {
			alert(error);
		});
	}

	$scope.remove = function(user) {
		$scope.backup = $scope.note.editors;
		$http.get("rest/removeUser", {
			params : {
				"messageId" : $scope.note.messageId,
				"user" : user.login
			}
		}).success(function(data) {
			$scope.note.editors = $scope.note.editors.filter(function(u) {
				return u.login !== user.login;
			});
		}).error(function(error, status) {
			alert(error);
		});
	}

});