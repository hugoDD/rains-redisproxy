# Quick Start

   * ### Clone & Build
      ```
      > git clone https://github.com/hugoDD/rains-redisproxy.git
   
      > cd rains-redisproxy
   
      > mvn -DskipTests clean install -U
      
   * ### 启动Eureka
     ```
     > java -jar rains-redisproxy-eureka-1.0.0-RELEASE.jar &
   
   * ### 启动redisProxy服务 
      ````
      > java -jar server-demo-1.0.0-RELEASE.jar &