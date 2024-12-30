## 基本資訊

*   Java version: 17
*   Spring boot version: 3.2.4

## Requirement

*   jre 17+
*   Docker &amp; Docker compose

## Port

1.  Http Server: 8081/tcp

## 機制

*   啟動時嘗試與 center 建立 RSocket 連線，若連線失敗會自動 retry，連線成功後斷線會自動 resume。
*   收到 center 傳來的新版排程設定後，會先檢查檔案是否已存在，僅下載不存在的檔案。
*   若下載檔案的 hash 與 file entity 中的不符，會回傳 DownlinkStatus = Error。
*   下載完成後會透過 Spring Quartz 建立排程，目前設定為，若排程因故未觸發，會在回復正常後觸發最後一次的排程並檢查當前是否需要播放。
*   排程觸發後透過呼叫 tb device http api 修改對應 device 的 attributes Style 及Schedule 的 id 作為 display dashboard 播放內容的依據。

## Setup

```
services:
  ds-edge:
    image: "5giotlead/ds-edge:1.0.0"
    restart: always
    depends_on:
      edgeplus:
        condition: service_healthy
    ports:
      - "8081:8081"
    environment:
      DATASOURCE_URL: jdbc:postgresql://postgres:5432/edgeplus
      SPRING_DATASOURCE_USERNAME: username
      SPRING_DATASOURCE_PASSWORD: password
      SPRING_QUARTZ_JDBC_INITIALIZE-SCHEMA: never
      SERVER_PORT: 8081
      TB_HOST: edgeplus
      TB_HTTP_PORT: 8080
      TB_SECURE: false
      CLOUD_RPC_HOST: serviceplex
      CLOUD_RSOCKET_PORT: 9898
      CLOUD_ROUTING_KEY: EDGE_KEY
      CLOUD_ROUTING_SECRET: EDGE_SECRET
      RSOCKET_CONNECT_RETRY_INTERVAL: 10
      RSOCKET_CONNECT_RESUME_INTERVAL: 10
    volumes:
      - /usr/share/nginx/.edgeplus-resource:/resource
```

1.  修改 docker-compose.yml

*   對應 edge 的 key 和 secret 。
*   ds center 的 host 和 rsocket port。
*   edgeplus 的 host 和 http port。
*   edgeplus 的 database 位置及 username / password。
*   第一次啟動時需要將環境變數 SPRING_QUARTZ_JDBC_INITIALIZE-SCHEMA 設為 always，後續記得改回 never，避免 quartz 排程被清空重建。

1.  啟動 ds-edge `docker compose up ds-edge -d`
2.  檢查 ds-center log 是否有建立連線 `<edge_id> connected.`
    * 查看 log - `docker compose logs -f ds-center`