## 개발 환경 초기 설정

### Mybatis 설정
- /resources/mapper/mapper-config.xml
- mapper 인터페이스에 바인딩할 xml 파일 설정
    - /resources/mapper/** 안에만 파일 형식이름을 맞추어 저장하면됨. 세부 디렉토리는 depth에 상관없음.(패턴으로 스캐닝하기 때문)
    - ##### maaper xml파일 이름 규약 : **Mapper.xml
    ```java
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfigLocation(this.pathMatchingResourcePatternResolver.getResource("classpath:mapper/mapper-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(this.pathMatchingResourcePatternResolver.getResources("classpath:mapper/**/*Mapper.xml"));

        return sqlSessionFactoryBean.getObject();
    }
    ```
    ##### setConfigLocation메서드와 setMapperLocations메서드를 확인
- MapperMapperScannerConfigurer클래스를 이용하여 mapper 인터페이스 스캐닝 -> @Mapper 애너테이션을 붙이지 않아도됨
    - com.naver.hackday.repository.origin 패키지 안에 mapper인터페이스를 넣어야함.
    ```java
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();

        mapperScannerConfigurer.setBasePackage("com.naver.hackday.repository.origin");
        mapperScannerConfigurer.setBeanName("sqlSessionFactory");

        return mapperScannerConfigurer;
    }
    ```
    ##### 위와 같이 DataSourceConfig.java에서 setBasePackage로 등록한 패키지를 스캐닝하기 때문임.

### Redis & Jedis설정
- /resources/redis.properties
- JedisPool설정 -> RedisConfig.java 파일 확인
