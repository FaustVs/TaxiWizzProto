'use strict';

angular.module('taxiwizzApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


