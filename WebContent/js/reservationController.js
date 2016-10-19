angular.module('app').controller('reservationController', function($http, $scope, items, $modal, $modalInstance) {
	$scope.backupData = undefined;

	$scope.movie = items[0];
	$scope.user = items[1];
	$scope.init = function() {
		$scope.rows = [];
		$scope.cols = [];
		for (var i = 1; i <= $scope.movie.rows; i++) {
			$scope.rows.push(i);
		}
		for (var i = 1; i <= $scope.movie.cols; i++) {
			$scope.cols.push(i);
		}
	};

	$scope.init();

	$scope.isMine = function(index, row, col) {
		var sum = $scope.getIndex(index, row, col);
		var reservation = $scope.movie.reservations.filter(function(obj) {
			return obj.user.login === $scope.user.login && obj.place === sum;
		})[0];
		return reservation !== undefined;
	}

	$scope.isReserved = function(index, row, col) {
		var sum = $scope.getIndex(index, row, col);
		return $scope.movie.reservations.filter(function(obj) {
			return obj.place === sum;
		})[0] !== undefined;
	}

	$scope.getIndex = function(index, row, col) {
		return $scope.movie.cols * index + col;
	}

	$scope.reserve = function(index, row, col) {
		var sum = $scope.getIndex(index, row, col);
		$scope.movie.reservations.push({
			place : sum,
			user : $scope.user
		});
		var response = confirm("Czy na pewno chcesz zarezerwować miejsce " + sum + "?");
		if (response) {
			$http.post('rest/reserve', $scope.movie).success(function(result) {
				if (result === "true") {
				} else {
					alert("Wystąpił błąd podczas rezerwacji");
				}
			});
		}
	}

	$scope.unreserve = function(index, row, col) {
		var sum = $scope.getIndex(index, row, col);
		var index = $scope.movie.reservations.findIndex(function(obj) {
			return obj.place === sum;
		});
		var response = confirm("Czy na pewno chcesz anulować rezerwację dla miejsca " + sum + "?");
		if (response) {
			$scope.movie.reservations.splice(index, 1);
			$http.post('rest/unreserve', $scope.movie).success(function(result) {
				if (result === "true") {
				} else {
					alert("Wystąpił błąd podczas anulowania rezerwacji");
				}
			});
		}
	}

	$scope.close = function() {
		$modalInstance.close();
	}
});