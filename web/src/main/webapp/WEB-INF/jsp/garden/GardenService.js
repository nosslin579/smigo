function GardenService(PlantService) {
    var model = {
        selectedSpecies: PlantService.getGarden().species["28"],
        selectedYear: +Object.keys(PlantService.getGarden().squares).sort().slice(-1).pop(),
        availableYears: PlantService.getAvailableYears()
    };
    return {model: model}
}
angular.module('smigoModule').factory('GardenService', GardenService);