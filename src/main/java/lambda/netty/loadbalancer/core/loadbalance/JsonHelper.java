/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lambda.netty.loadbalancer.core.loadbalance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;

public class JsonHelper<T> {
    final static Logger logger = Logger.getLogger(JsonHelper.class);


    private static ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> t;

    protected JsonHelper(Class<T> t) {

        this.t = t;
    }


    public String ObjToJson(Object obj) {
        String tmp = null;
        try {
            tmp = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Cannot process the Object !", e);
        }
        return tmp;
    }

    public T jsonToObject(String str) {
        T tmp = null;
        try {
            tmp = objectMapper.readValue(str, t);
        } catch (IOException e) {
            logger.error("Cannot parse the String !", e);
        }
        return tmp;
    }

}
