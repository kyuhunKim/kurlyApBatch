package com.lgcns.wcs.kurly.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.lgcns.wcs.kurly.dto.DasNumUseCellData;
import com.lgcns.wcs.kurly.dto.InvoicePrintCompletData;
import com.lgcns.wcs.kurly.dto.InvoiceSortCompletData;
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyData;
import com.lgcns.wcs.kurly.dto.OrdmadeNotfullyReplayData;
import com.lgcns.wcs.kurly.dto.PackQpsCompletData;
import com.lgcns.wcs.kurly.dto.PickQpsCompletData;
import com.lgcns.wcs.kurly.dto.QpsNumUseCellData;
import com.lgcns.wcs.kurly.dto.ToteCellExceptTxnData;
import com.lgcns.wcs.kurly.dto.ToteReleaseParamData;
import com.lgcns.wcs.kurly.dto.ToteScanData;

@Configuration
public class KurlyProducerConfig {

    @Value("${spring.kafka.brokers.producer}")
    private String BOOTSTRAP_SERVER;

    /**
	 * 
	 * @Method Name : toteReleaseKafkaTemplate
	 * @작성일 : 2020. 07. 10.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 10. 최초작성
	 * @Method 설명 : 토트 마스트 초기화 연계  kafkaTemplate
	 */
    @Bean("toteReleaseKafkaTemplate")
    public KafkaTemplate<String, ToteReleaseParamData> toteReleaseKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "10000");
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));
    }
 
    /**
	 * 
	 * @Method Name : toteScanKafkaTemplate
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 토트 자동화 설비 투입 정보 (마스터)  kafkaTemplate
	 */
    @Bean("toteScanKafkaTemplate")
    public KafkaTemplate<String, ToteScanData> toteScanKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }

    /**
	 * 
	 * @Method Name : toteCellExceptTxnKafkaTemplate
	 * @작성일 : 2020. 07. 14.
	 * @작성자 : jooni
	 * @변경이력 : 2020. 07. 14. 최초작성
	 * @Method 설명 : WCS 문제처리용 피킹정보 연계  kafkaTemplate
	 */
    @Bean("toteCellExceptTxnKafkaTemplate")
    public KafkaTemplate<String, ToteCellExceptTxnData> toteCellExceptTxnKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }

    /**
   	 * 
   	 * @Method Name : ordmadeNotfullyReplayKafkaTemplate
   	 * @작성일 : 2020. 07. 15.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 07. 15. 최초작성
   	 * @Method 설명 :  WCS 미출오더 상품보충용 추가피킹정보 연계 kafkaTemplate
	 */
    @Bean("ordmadeNotfullyReplayKafkaTemplate")
    public KafkaTemplate<String, OrdmadeNotfullyReplayData> ordmadeNotfullyReplayKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }
    /**
   	 * 
   	 * @Method Name : ordmadeNotfullyKafkaTemplate
   	 * @작성일 : 2020. 07. 15.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 07. 15. 최초작성
   	 * @Method 설명 :  WCS 미출오더 처리시 WMS 피킹지시 금지 정보 연계 kafkaTemplate
	 */
    @Bean("ordmadeNotfullyKafkaTemplate")
    public KafkaTemplate<String, OrdmadeNotfullyData> ordmadeNotfullyKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }
    /**
   	 * 
   	 * @Method Name : pickQpsCompletKafkaTemplate
   	 * @작성일 : 2020. 07. 15.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 07. 15. 최초작성
   	 * @Method 설명 : WCS 오더 피킹 완료 정보 kafkaTemplate
	 */
    @Bean("pickQpsCompletKafkaTemplate")
    public KafkaTemplate<String, PickQpsCompletData> pickQpsCompletKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }
    /**
   	 * 
   	 * @Method Name : packQpsCompletKafkaTemplate
   	 * @작성일 : 2020. 07. 15.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 07. 15. 최초작성
   	 * @Method 설명 : WCS 오더 패킹 완료 정보 kafkaTemplate
	 */
    @Bean("packQpsCompletKafkaTemplate")
    public KafkaTemplate<String, PackQpsCompletData> packQpsCompletKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }
    /**
   	 * 
   	 * @Method Name : invoicePrintCompletKafkaTemplate
   	 * @작성일 : 2020. 07. 15.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 07. 15. 최초작성
   	 * @Method 설명 : WCS 운송장 발행 정보 kafkaTemplate
	 */
    @Bean("invoicePrintCompletKafkaTemplate")
    public KafkaTemplate<String, InvoicePrintCompletData> invoicePrintCompletKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }
    /**
   	 * 
   	 * @Method Name : invoiceSortCompletKafkaTemplate
   	 * @작성일 : 2020. 07. 15.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 07. 15. 최초작성
   	 * @Method 설명 : WCS 방면 분류 완료 정보 kafkaTemplate
	 */
    @Bean("invoiceSortCompletKafkaTemplate")
    public KafkaTemplate<String, InvoiceSortCompletData> invoiceSortCompletKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }

    /**
   	 * 
   	 * @Method Name : qpsNumUseCellKafkaTemplate
   	 * @작성일 : 2020. 08. 25.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 08. 25. 최초작성
   	 * @Method 설명 : QPS 호기별 가용셀 정보 kafkaTemplate
	 */
    @Bean("qpsNumUseCellKafkaTemplate")
    public KafkaTemplate<String, QpsNumUseCellData> qpsNumUseCellKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }

    /**
   	 * 
   	 * @Method Name : dasNumUseCellKafkaTemplate
   	 * @작성일 : 2020. 11. 24.
   	 * @작성자 : jooni
   	 * @변경이력 : 2020. 11. 24. 최초작성
   	 * @Method 설명 : DAS 호기별 가용셀 정보 kafkaTemplate
	 */
    @Bean("dasNumUseCellKafkaTemplate")
    public KafkaTemplate<String, DasNumUseCellData> dasNumUseCellKafkaTemplate(){
        Map<String, Object> configPros = new HashMap<>();

        configPros.put(ProducerConfig.RETRIES_CONFIG, 1);
        configPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configPros));    	
    }
}