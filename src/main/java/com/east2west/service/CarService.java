package com.east2west.service;

import com.east2west.models.DTO.*;
import com.east2west.models.Entity.*;
import com.east2west.models.mapper.CarMapper;
import com.east2west.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.east2west.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.east2west.models.payload.request.CarFilterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarReviewRepository carReviewRepository;

    private final CarRepository carRepository;


    private final ModelRepository modelRepository;


    private final CarAvailabilityRepository carAvailabilityRepository;

    private final TypeRepository typeRepository;


    private final LocationTypeRepository locationTypeRepository;

    private final WardRepository wardRepository;


    private final CityProvinceRepository cityProvinceRepository;


    private final DistrictRepository districtRepository;

    @Autowired
    public CarService(CarReviewRepository carReviewRepository, CarRepository carRepository, ModelRepository modelRepository, CarAvailabilityRepository carAvailabilityRepository, TypeRepository typeRepository, LocationTypeRepository locationTypeRepository, WardRepository wardRepository, CityProvinceRepository cityProvinceRepository, DistrictRepository districtRepository) {
        this.carReviewRepository = carReviewRepository;
        this.carRepository = carRepository;
        this.modelRepository = modelRepository;
        this.carAvailabilityRepository = carAvailabilityRepository;
        this.typeRepository = typeRepository;
        this.locationTypeRepository = locationTypeRepository;
        this.wardRepository = wardRepository;
        this.cityProvinceRepository = cityProvinceRepository;
        this.districtRepository = districtRepository;
    }

    public Optional<Car> findByCarName(String carName) {
        return carRepository.findByCarName(carName);
    }

    public CarDTO createCar(@NotNull CarDTO carDTO) {
        GeometryFactory geometryFactory = new GeometryFactory();

        CityProvince cityProvince = new CityProvince();
        cityProvince.setCityname(carDTO.getCityProvinceName());
        cityProvince = cityProvinceRepository.save(cityProvince);

        District district = new District();
        district.setDistrictname(carDTO.getDistrictName());
        district.setCityprovince(cityProvince);
        district = districtRepository.save(district);

        Ward ward = new Ward();
        ward.setWardname(carDTO.getWardName());
        ward.setDistrict(district);
        ward = wardRepository.save(ward);

        Point point = geometryFactory.createPoint(new Coordinate(carDTO.getLongitude(), carDTO.getLatitude()));

        Car.CarBuilder carBuilder = Car.builder()
                .carName(carDTO.getCarName())
                .quantity(carDTO.getQuantity())
                .status(carDTO.getStatus())
                .year(carDTO.getYear())
                .seatCapacity(carDTO.getSeatCapacity())
                .fueltankcapacity(carDTO.getFueltankcapacity())
                .enginesystem(carDTO.getEngineSystem())
                .fuel(carDTO.getFuel())
                .miles(carDTO.getMiles())
                .cargearbox(carDTO.getCargearbox())
                .longitude(carDTO.getLongitude())
                .latitude(carDTO.getLatitude())
                .airConditioned(carDTO.isAirConditioned())
                .location(carDTO.getLocation())
                .fourDoorsOrMore(carDTO.isFourDoorsOrMore())
                .geom(point)
                .thumbnail(carDTO.getThumbnail())
                .smallLuggage(carDTO.getSmallLuggage())
                .largeLuggage(carDTO.getLargeLuggage())
                .deposit(carDTO.getDeposit())
                .cancelFree(carDTO.getCancelFree())
                .fuelSameReturn(carDTO.getFuelSameReturn())
                .ward(ward);

        modelRepository.findById(carDTO.getModel().getModelid()).ifPresent(carBuilder::model);
        typeRepository.findById(carDTO.getType().getTypeid()).ifPresent(carBuilder::type);
        locationTypeRepository.findById(carDTO.getLocationType().getLocationtypeid()).ifPresent(carBuilder::locationtype);

        Car savedCar = carRepository.save(carBuilder.build());

        if (carDTO.getPricePerDay() != null) {
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(365);
            List<CarAvailability> carAvailabilityList = new ArrayList<>();

            while (!today.isAfter(endDate)) {
                CarAvailability availability = new CarAvailability();
                availability.setCar(savedCar);
                availability.setDate(Timestamp.valueOf(today.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime()));
                availability.setAvailableQuantity(carDTO.getQuantity());
                availability.setPricePerDay(BigDecimal.valueOf(carDTO.getPricePerDay()));
                carAvailabilityList.add(availability);
                today = today.plusDays(1);
            }

            carAvailabilityRepository.saveAll(carAvailabilityList);

            savedCar.setCarAvailabilityList(carAvailabilityList);
        }

        return CarMapper.INSTANCE.toDTO(savedCar);
    }


    public CarDTO updateCar(@NotNull CarDTO carDTO) {
        GeometryFactory geometryFactory = new GeometryFactory();
        CityProvince cityProvince = new CityProvince();
        cityProvince.setCityname(carDTO.getCityProvinceName());
        cityProvince = cityProvinceRepository.save(cityProvince);


        District district = new District();
        district.setDistrictname(carDTO.getDistrictName());
        district.setCityprovince(cityProvince);
        district = districtRepository.save(district);


        Ward ward = new Ward();
        ward.setWardname(carDTO.getWardName());
        ward.setDistrict(district);
        ward = wardRepository.save(ward);


        Point point = geometryFactory.createPoint(new Coordinate(carDTO.getLongitude(), carDTO.getLatitude()));
        Car.CarBuilder carBuilder = Car.builder()
                .carid(carDTO.getCarid())
                .carName(carDTO.getCarName())
                .quantity(carDTO.getQuantity())
                .status(carDTO.getStatus())
                .year(carDTO.getYear())
                .seatCapacity(carDTO.getSeatCapacity())
                .fueltankcapacity(carDTO.getFueltankcapacity())
                .enginesystem(carDTO.getEngineSystem())
                .fuel(carDTO.getFuel())
                .miles(carDTO.getMiles())
                .cargearbox(carDTO.getCargearbox())
                .longitude(carDTO.getLongitude())
                .latitude(carDTO.getLatitude())
                .airConditioned(carDTO.isAirConditioned())
                .location(carDTO.getLocation())
                .fourDoorsOrMore(carDTO.isFourDoorsOrMore())
                .geom(point)
                .fueltankcapacity(carDTO.getFueltankcapacity())
                .thumbnail(carDTO.getThumbnail())
                .smallLuggage(carDTO.getSmallLuggage())
                .largeLuggage(carDTO.getLargeLuggage())
                .deposit(carDTO.getDeposit())
                .cancelFree(carDTO.getCancelFree())
                .fuelSameReturn(carDTO.getFuelSameReturn())
                .ward(ward);
        modelRepository.findById(carDTO.getModel().getModelid()).ifPresent(carBuilder::model);
        typeRepository.findById(carDTO.getType().getTypeid()).ifPresent(carBuilder::type);
        locationTypeRepository.findById(carDTO.getLocationType().getLocationtypeid()).ifPresent(carBuilder::locationtype);


        Car car = carBuilder.build();
        if (carDTO.getPricePerDay() != null) {
            LocalDate today = LocalDate.now();
            carAvailabilityRepository.deleteAllByCarAndDateAfter(car, Timestamp.valueOf(today.atStartOfDay()));

            LocalDate endDate = today.plusDays(365);
            List<CarAvailability> newAvailabilities = new ArrayList<>();

            while (!today.isAfter(endDate)) {
                CarAvailability availability = new CarAvailability();
                availability.setCar(car);
                availability.setDate(Timestamp.valueOf(today.atStartOfDay()));
                availability.setAvailableQuantity(carDTO.getQuantity());
                availability.setPricePerDay(BigDecimal.valueOf(carDTO.getPricePerDay()));
                newAvailabilities.add(availability);
                today = today.plusDays(1);
            }

            car.setCarAvailabilityList(newAvailabilities);
        }

        return CarMapper.INSTANCE.toDTO(carRepository.save(car));
    }

    public Page<CarDTO> getAllCars(Pageable pageable) {
        Page<Car> carPage = carRepository.findAll(pageable);
        return carPage.map(car -> {
            CarDTO dto = CarMapper.INSTANCE.toDTO(car);
            Double avg = carReviewRepository.getAverageRatingByCar(car.getCarid());
            int numberOfReviews = carReviewRepository.findByCar_Carid(car.getCarid()).size();
            CarReviewAverageDTO reviewAverage = carReviewRepository.getAverageDetailsByCar(car.getCarid());
            dto.setAverageRating(avg != null ? avg : 0.0);
            dto.setNumberOfReviews(numberOfReviews);
            dto.setReviewAverage(reviewAverage);
            return dto;
        });
    }

    public String deleteCar(int id) {

        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            carRepository.deleteById(id);
            return "Deleted " + car.get().getCarName() + " car successfully";
        } else {
            return "Not found car";
        }
    }

    public Optional<CarDTO> getCarById(int id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            Double averageRating = carReviewRepository.getAverageRatingByCar(id);
            int numberOfReviews = carReviewRepository.findByCar_Carid(id).size();
            CarReviewAverageDTO reviewAverage = carReviewRepository.getAverageDetailsByCar(id);
            CarDTO carDTO = CarMapper.INSTANCE.toDTO(car.get());
            carDTO.setAverageRating(averageRating);
            carDTO.setNumberOfReviews(numberOfReviews);
            carDTO.setReviewAverage(reviewAverage);
            return Optional.of(carDTO);
        }
        return Optional.empty();
    }

    public List<CarDTO> searchCar(String keyword) {
        List<Car> car = carRepository.findByCarNameContainingIgnoreCase(keyword);
        return car.stream().map(CarMapper.INSTANCE::toDTO).toList();
    }


    public String saveCarFromCSV(@NotNull MultipartFile[] files) {
        try {
            List<Car> carsList = new ArrayList<>();
            Optional<Car> maxIdCar = carRepository.findAll().stream()
                    .max(Comparator.comparingInt(Car::getCarid));
            int idCounter = maxIdCar.map(car -> car.getCarid() + 1)
                    .orElse(1);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String line;
                int lineNumber = 0;
                Map<String, Integer> headerMap = new HashMap<>();

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    if (lineNumber == 1) {

                        String[] headers = line.split(",");
                        for (int i = 0; i < headers.length; i++) {
                            headerMap.put(headers[i].trim(), i);
                        }
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length < headerMap.size()) {
                        continue;
                    }

                    String CarName = getValueByHeader(data, headerMap, "CarName");
                    String Quantity = getValueByHeader(data, headerMap, "Quantity");
                    String PricePerDay = getValueByHeader(data, headerMap, "PricePerDay");
                    String Status = getValueByHeader(data, headerMap, "Status");
                    String TypeName = getValueByHeader(data, headerMap, "TypeName");
                    String MakeName = getValueByHeader(data, headerMap, "MakeName");
                    String ModelName = getValueByHeader(data, headerMap, "ModelName");
                    String Year = getValueByHeader(data, headerMap, "Year");
                    String SeatCapacity = getValueByHeader(data, headerMap, "SeatCapacity");
                    String FuelTankCapacity = getValueByHeader(data, headerMap, "FuelTankCapacity");
                    String EngineSystem = getValueByHeader(data, headerMap, "EngineSystem");
                    String Fuel = getValueByHeader(data, headerMap, "Fuel").replace(";", ",");
                    String Miles = getValueByHeader(data, headerMap, "Miles");
                    String Gearbox = getValueByHeader(data, headerMap, "Gearbox");
                    String LocationTypeName = getValueByHeader(data, headerMap, "LocationTypeName");
                    String LocationTypeDescription = getValueByHeader(data, headerMap, "LocationTypeDescription");
                    String AirConditioned = getValueByHeader(data, headerMap, "AirConditioned");
                    String FourDoorsOrMore = getValueByHeader(data, headerMap, "FourDoorsOrMore");
                    String Latitude = getValueByHeader(data, headerMap, "Latitude");
                    String Longitude = getValueByHeader(data, headerMap, "Longitude");
                    String Location = getValueByHeader(data, headerMap, "Location");
                    String WardName = getValueByHeader(data, headerMap, "WardName");
                    String DistrictName = getValueByHeader(data, headerMap, "DistrictName");
                    String CityProvinceName = getValueByHeader(data, headerMap, "CityProvinceName");
                    String Thumbnails = getValueByHeader(data, headerMap, "Thumbnails").replace(";", ",");

                    Optional<LocationType> locationType = locationTypeRepository.findByLocationtypenameAndLocationtypedescription(LocationTypeName, LocationTypeDescription);
                    if (locationType.isEmpty()) {
                        return "Line: " + lineNumber + " column: " +
                                findHeaderName(headerMap, "LocationTypeName") + "/" +
                                findHeaderName(headerMap, "LocationTypeDescription") +
                                " - Location Type are invalid !!!";
                    }

                    Optional<Type> type = typeRepository.findByTypename(TypeName);
                    if (type.isEmpty()) {
                        return "Line: " + lineNumber + " column: " +
                                findHeaderName(headerMap, "TypeName") +
                                " - Type are invalid !!!";
                    }

                    Optional<Model> model = modelRepository.findByMake_MakenameAndModelname(MakeName, ModelName);
                    if (model.isEmpty()) {
                        return "Line: " + lineNumber + " column: " +
                                findHeaderName(headerMap, "MakeName") + "/" +
                                findHeaderName(headerMap, "ModelName") +
                                " - Make or Model are invalid !!!";
                    }

                    GeometryFactory geometryFactory = new GeometryFactory();
                    Point point = geometryFactory.createPoint(new Coordinate(Double.parseDouble(Longitude), Double.parseDouble(Latitude)));

                    System.out.println("Đang xử lý tiện ích: " + CarName);

                    CityProvince cityProvince = new CityProvince();
                    cityProvince.setCityname(CityProvinceName);
                    cityProvince = cityProvinceRepository.save(cityProvince);

                    District district = new District();
                    district.setDistrictname(DistrictName);
                    district.setCityprovince(cityProvince);
                    district = districtRepository.save(district);

                    Ward ward = new Ward();
                    ward.setWardname(WardName);
                    ward.setDistrict(district);
                    ward = wardRepository.save(ward);

                    Car car = Car.builder()
                            .carid(idCounter++)
                            .carName(CarName)
                            .quantity(Integer.parseInt(Quantity))
                            .status(Status)
                            .type(type.orElse(null))
                            .model(model.orElse(null))
                            .year(Integer.parseInt(Year))
                            .seatCapacity(Integer.parseInt(SeatCapacity))
                            .fueltankcapacity(FuelTankCapacity)
                            .enginesystem(EngineSystem)
                            .fuel(Fuel)
                            .miles(Miles)
                            .cargearbox(Gearbox)
                            .locationtype(locationType.orElse(null))
                            .airConditioned(AirConditioned.equalsIgnoreCase("yes"))
                            .fourDoorsOrMore(FourDoorsOrMore.equalsIgnoreCase("yes"))
                            .geom(point)
                            .latitude(Double.parseDouble(Latitude))
                            .longitude(Double.parseDouble(Longitude))
                            .location(Location)
                            .ward(ward)
                            .thumbnail(Arrays.asList(Thumbnails.split(",")))
                            .build();
                    carsList.add(car);
                }
            }
            carRepository.saveAll(carsList);
            return "" + carsList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

    private String getValueByHeader(String[] data, Map<String, Integer> headerMap, String headerName) {
        Integer index = headerMap.get(headerName);
        if (index != null && index < data.length) {
            return data[index].trim();
        }
        return "";
    }

    private String findHeaderName(Map<String, Integer> headerMap, String defaultName) {
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            if (entry.getKey().trim().equalsIgnoreCase(defaultName.trim())) {
                return entry.getKey();
            }
        }
        return defaultName;
    }


    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(car -> {
            CarDTO dto = CarMapper.INSTANCE.toDTO(car);
            Double avg = carReviewRepository.getAverageRatingByCar(car.getCarid());
            int numberOfReviews = carReviewRepository.findByCar_Carid(car.getCarid()).size();
            CarReviewAverageDTO reviewAverage = carReviewRepository.getAverageDetailsByCar(car.getCarid());
            dto.setAverageRating(avg != null ? avg : 0.0);
            dto.setNumberOfReviews(numberOfReviews);
            dto.setReviewAverage(reviewAverage);
            return dto;
        }).toList();
    }


    public List<CarDTO> searchCars(CarSearchDTO request) {


        var pickUpDate = request.getPickUpDate();
        var dropOffDate = request.getDropOffDate();


        int days = (int) DateUtil.getDiffInDays(pickUpDate, dropOffDate);
        dropOffDate = dropOffDate.minusDays(1);

        List<Car> cars = carRepository.searchCar(
                request.getLongitude(),
                request.getLatitude(),
                request.getRadius(),
                pickUpDate,
                dropOffDate,
                days

        );
        return cars.stream()
                .filter(car -> car.getStatus().equalsIgnoreCase("available"))
                .map(car -> {
                    CarDTO dto = CarMapper.INSTANCE.toDTO(car);
                    Double avg = carReviewRepository.getAverageRatingByCar(car.getCarid());
                    int numberOfReviews = carReviewRepository.findByCar_Carid(car.getCarid()).size();
                    CarReviewAverageDTO reviewAverage = carReviewRepository.getAverageDetailsByCar(car.getCarid());
                    dto.setAverageRating(avg != null ? avg : 0.0);
                    dto.setNumberOfReviews(numberOfReviews);
                    dto.setReviewAverage(reviewAverage);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private double[] getRatingRange(String ratingText) {
        return switch (ratingText.toLowerCase()) {
            case "excellent" -> new double[]{9.5, 10};
            case "great" -> new double[]{9.0, 9.5};
            case "very good" -> new double[]{8.0, 9.0};
            case "good" -> new double[]{7.0, 8.0};
            case "rather" -> new double[]{6.0, 7.0};
            case "medium" -> new double[]{5.0, 6.0};
            case "bad" -> new double[]{0, 5.0};
            default -> null;
        };
    }

    private List<String> safeList(List<String> list) {
        return list == null ? Collections.emptyList() : list;
    }



    public List<CarDTO> filterCars(CarFilterRequest request) {

        String sort = request.getSort();
        List<String> gearbox = request.getGearbox();
        List<String> type = request.getType();
        List<String> information = request.getInformation();
        List<String> engine = request.getEngine();
        List<String> mileage = request.getMileage();
        List<String> make = request.getMake();
        List<String> location = request.getLocation();
        String fuelPolicy = request.getFuelPolicy();
        List<String> rating = request.getRating();
        List<String> deposit = request.getDeposit();
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();
        LocalDate pickupDate = request.getPickupDate();
        LocalDate dropOffDate = request.getDropOffDate();
        Double radius = request.getRadius();

        // Null-safe
        List<String> gearboxList = safeList(gearbox);
        List<String> typeList = safeList(type);
        List<String> infoList = safeList(information);
        List<String> engineList = safeList(engine);
        List<String> mileageList = safeList(mileage);
        List<String> makeList = safeList(make);
        List<String> locationList = safeList(location);
        List<String> ratingList = safeList(rating);
        List<String> depositList = safeList(deposit);


        List<Type> types = typeList.stream()
                .map(t -> Type.builder().typename(t).build())
                .toList();
        List<Make> makes = makeList.stream()
                .map(m -> Make.builder().makename(m).build())
                .toList();
        List<double[]> depositRanges = depositList.stream()
                .map(d -> {
                    String[] parts = d.split("-");
                    double min = Double.parseDouble(parts[0].trim());
                    double max = parts[1].trim().equalsIgnoreCase("Infinity") ? Double.MAX_VALUE : Double.parseDouble(parts[1].trim());
                    return new double[]{min, max};
                })
                .toList();
        List<LocationType> locationTypes = locationList.stream()
                .map(loc -> loc.split("-", 2))
                .filter(parts -> parts.length == 2)
                .map(parts -> LocationType.builder()
                        .locationtypename(parts[0].trim())
                        .locationtypedescription(parts[1].trim())
                        .build())
                .toList();
        List<double[]> ratingRanges = ratingList.stream()
                .map(this::getRatingRange)
                .filter(Objects::nonNull)
                .toList();
        int days = (int) DateUtil.getDiffInDays(pickupDate, dropOffDate);
        dropOffDate = dropOffDate.minusDays(1);
        List<Car> cars = carRepository.searchCar(longitude, latitude, radius, pickupDate, dropOffDate, days);
        List<CarDTO> result = cars.stream()
                .filter(car -> car.getStatus().equalsIgnoreCase("available"))
                .map(car -> {
                    CarDTO dto = CarMapper.INSTANCE.toDTO(car);
                    Double avg = carReviewRepository.getAverageRatingByCar(car.getCarid());
                    int numberOfReviews = carReviewRepository.findByCar_Carid(car.getCarid()).size();
                    CarReviewAverageDTO reviewAverage = carReviewRepository.getAverageDetailsByCar(car.getCarid());
                    dto.setAverageRating(avg != null ? avg : 0.0);
                    dto.setNumberOfReviews(numberOfReviews);
                    dto.setReviewAverage(reviewAverage);
                    return dto;
                })
                .filter(car -> types.isEmpty() || types.stream().anyMatch(t -> t.getTypename().equals(car.getType().getTypename())))
                .filter(car -> makes.isEmpty() || makes.stream().anyMatch(m -> m.getMakename().equals(car.getMake().getMakename())))
                .filter(car -> ratingRanges.isEmpty() || ratingRanges.stream().anyMatch(r -> car.getAverageRating() >= r[0] && car.getAverageRating() <= r[1]))
                .filter(car -> {
                    if (depositRanges.isEmpty()) return true;
                    Double carDepositPercent = car.getDeposit() != null ? car.getDeposit().doubleValue() : null;
                    double carPrice = car.getPricePerDay() != null ? car.getPricePerDay() : 0;
                    if (carDepositPercent == null || carPrice == 0) return false;
                    double depositAmount = carPrice * carDepositPercent / 100;
                    return depositRanges.stream()
                            .anyMatch(d -> depositAmount >= d[0] && depositAmount < d[1]);
                })
                .filter(car -> gearboxList.isEmpty() || gearboxList.stream().anyMatch(g -> g.equalsIgnoreCase(car.getCargearbox())))
                .filter(car -> {
                    if (engineList.isEmpty()) return true;
                    String carEngine = car.getEngineSystem();
                    if (carEngine == null) return false;
                    return engineList.stream()
                            .anyMatch(e -> e.equalsIgnoreCase(carEngine.trim()));
                })
                .filter(car -> {
                    if (infoList.isEmpty()) return true;
                    boolean containsAir = infoList.stream().anyMatch(i -> i.trim().equalsIgnoreCase("Air Conditioner"));
                    return !containsAir || car.isAirConditioned();
                })
                .filter(car -> {
                    if (infoList.isEmpty()) return true;
                    boolean containsDoors = infoList.stream().anyMatch(i -> i.trim().equalsIgnoreCase("+4 Doors"));
                    return !containsDoors || car.isFourDoorsOrMore();
                })
                .filter(car -> locationTypes.isEmpty() || locationTypes.stream().anyMatch(loc ->
                        loc.getLocationtypename().equals(car.getLocationType().getLocationtypename()) &&
                                loc.getLocationtypedescription().equals(car.getLocationType().getLocationtypedescription())
                ))
                .filter(car -> !"Return vehicle with fuel as is".equals(fuelPolicy) || Boolean.TRUE.equals(car.getFuelSameReturn()))
                .filter(car -> {
                    if (mileageList.isEmpty()) return true;

                    boolean wantsUnlimited = mileageList.stream().anyMatch(m -> m.equalsIgnoreCase("Unlimited"));
                    boolean wantsLimited = mileageList.stream().anyMatch(m -> m.equalsIgnoreCase("Limited"));

                    String carMiles = car.getMiles();
                    if (carMiles == null) return false;

                    if (wantsUnlimited && "Unlimited".equalsIgnoreCase(carMiles)) return true;
                    return wantsLimited && !"Unlimited".equalsIgnoreCase(carMiles);
                })

                .toList();

        if (sort != null) {
            switch (sort) {
                case "Recommended" -> result = result.stream().toList();
                case "PriceLowToHigh" ->
                        result = result.stream()
                                .sorted(Comparator.comparing(CarDTO::getPricePerDay, Comparator.nullsLast(Double::compareTo)))
                                .toList();

                case "TopRated" ->
                        result = result.stream()
                                .sorted(Comparator.comparing(CarDTO::getAverageRating, Comparator.nullsLast(Double::compareTo)).reversed())
                                .toList();

            }
        }
        return result;
    }

}
