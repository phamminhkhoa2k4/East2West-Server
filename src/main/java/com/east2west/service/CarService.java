package com.east2west.service;

import com.east2west.models.Entity.*;
import com.east2west.models.mapper.CarMapper;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.east2west.repository.*;
import com.east2west.models.DTO.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CarService {

    private final CarRepository carRepository;


    private final ModelRepository modelRepository;

    private final TypeRepository typeRepository;


    private final LocationTypeRepository locationTypeRepository;

    private final WardRepository wardRepository;


    private final CityProvinceRepository cityProvinceRepository;


    private final DistrictRepository districtRepository;
    @Autowired
    public CarService(CarRepository carRepository, ModelRepository modelRepository, TypeRepository typeRepository,LocationTypeRepository locationTypeRepository, WardRepository wardRepository, CityProvinceRepository cityProvinceRepository, DistrictRepository districtRepository) {
        this.carRepository = carRepository;
        this.modelRepository = modelRepository;
        this.typeRepository = typeRepository;
        this.locationTypeRepository = locationTypeRepository;
        this.wardRepository = wardRepository;
        this.cityProvinceRepository = cityProvinceRepository;
        this.districtRepository = districtRepository;
    }
    public  Optional<Car> findByCarName(String carName){
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
                .pricePerDay(carDTO.getPricePerDay())
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
                .ward(ward);
        modelRepository.findById(carDTO.getModel().getModelid()).ifPresent(carBuilder::model);
        typeRepository.findById(carDTO.getType().getTypeid()).ifPresent(carBuilder::type);
        locationTypeRepository.findById(carDTO.getLocationType().getLocationtypeid()).ifPresent(carBuilder::locationtype);
        Car car = carBuilder.build();

        return CarMapper.INSTANCE.toDTO(carRepository.save(car));
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
                .pricePerDay(carDTO.getPricePerDay())
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
                .ward(ward);
        modelRepository.findById(carDTO.getModel().getModelid()).ifPresent(carBuilder::model);
        typeRepository.findById(carDTO.getType().getTypeid()).ifPresent(carBuilder::type);
        locationTypeRepository.findById(carDTO.getLocationType().getLocationtypeid()).ifPresent(carBuilder::locationtype);
        Car car = carBuilder.build();
        return CarMapper.INSTANCE.toDTO(carRepository.save(car));
    }
    public Page<CarDTO> getAllCars(Pageable pageable) {
        Page<Car>  carPage = carRepository.findAll(pageable);
        return carPage.map(CarMapper.INSTANCE::toDTO);
    }
    public String deleteCar(int id){

        Optional<Car> car = carRepository.findById(id);
        if(car.isPresent()){
            carRepository.deleteById(id);
            return "Deleted " + car.get().getCarName() + " car successfully";
        }else{
            return "Not found make";
        }
    }

    public Optional<CarDTO> getCarById(int id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()){
            CarDTO carDTO  = CarMapper.INSTANCE.toDTO(car.get());
            return Optional.ofNullable(carDTO);
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
            int idCounter = maxIdCar.map(make -> make.getCarid() + 1)
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
                    String PricePerDay = getValueByHeader(data, headerMap, "PricePerDay");
                    String Status = getValueByHeader(data, headerMap, "Status");
                    String TypeName = getValueByHeader(data, headerMap, "TypeName");
                    String MakeName = getValueByHeader(data, headerMap, "MakeName");
                    String ModelName = getValueByHeader(data, headerMap, "ModelName");
                    String Year = getValueByHeader(data, headerMap, "Year");
                    String SeatCapacity = getValueByHeader(data, headerMap, "SeatCapacity");
                    String FuelTankCapacity = getValueByHeader(data, headerMap, "FuelTankCapacity");
                    String EngineSystem = getValueByHeader(data, headerMap, "EngineSystem");
                    String Fuel = getValueByHeader(data, headerMap, "Fuel").replace(";",",");
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
                    String Thumbnails = getValueByHeader(data, headerMap, "Thumbnails").replace(";",",");

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
                            .pricePerDay(Double.parseDouble(PricePerDay))
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
}