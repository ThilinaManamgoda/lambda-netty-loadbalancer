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

import lambda.netty.loadbalancer.core.etcd.EtcdClientException;
import lambda.netty.loadbalancer.core.etcd.EtcdUtil;
import lambda.netty.loadbalancer.core.loadbalance.statemodels.InstanceStates;
import lambda.netty.loadbalancer.core.loadbalance.statemodels.State;

public class StateJsonHelp extends JsonHelper<State> {

    private static StateJsonHelp stateJsonHelp = new StateJsonHelp(State.class);

    private StateJsonHelp(Class<State> t) {
        super(t);
    }

    public static State getObject(String str){
       return stateJsonHelp.jsonToObject(str);
    }

    public static String toString(State state){
        return stateJsonHelp.ObjToJson(state);
    }


    public static void main(String[] args) {
//
//
//        State state = new State();
//
//        state.setHost("localhost");
//        state.setState(InstanceStates.DOWN);
//
//        try {
//            EtcdUtil.putValue("localhost",StateJsonHelp.toString(state));
//
//            EtcdUtil.getValue("localhost").thenAccept(x-> System.out.println(x.get));
//            Thread.sleep(5000);
//        } catch (EtcdClientException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
