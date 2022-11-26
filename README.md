issue 
1. 검색기능 API 속도 너무느림 
    * 문제점은 db를 통해서 새로운 productList를 받아오기 때문에 느림 필드에있는 list를 사용할것 

refactor 
1. makeProductPanel 시간 로그 찍어봄
2. new ProductContent 생성 문제점 발견
3. 또 내부적으로 image 생성시간 오래걸림 찾음 