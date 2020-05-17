package cn.com.paic.pazl.pinmonitorkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class PinTestMonitor {
    private static final Logger LOG = LoggerFactory.getLogger(PinTestMonitor.class);

    private static final Long LAG = 10 * 60 * 1000L; //10分钟

    //每隔s秒向kafka发送一个当前毫秒数
    @Bean
    public Supplier<Long> pin(){
        return ()->{
          return System.currentTimeMillis();
        };
    }


    @Bean
    public Consumer<Long> check(){
        return data->{
            Long currentMills = System.currentTimeMillis();
            if (currentMills - data > LAG){//从kafka接收到的消息延迟超过10分钟则说明出问题了
                LOG.error("收到消息延时太大");
            }
            else {
                LOG.info("OK");
            }
        };
    }
}
