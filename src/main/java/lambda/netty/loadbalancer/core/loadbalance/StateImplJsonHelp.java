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

import lambda.netty.loadbalancer.core.loadbalance.statemodels.State;
import lambda.netty.loadbalancer.core.loadbalance.statemodels.StateImpl;

public class StateImplJsonHelp extends JsonHelper<StateImpl> {

    private static StateImplJsonHelp stateImplJsonHelp = new StateImplJsonHelp(StateImpl.class);

    private StateImplJsonHelp(Class<StateImpl> t) {
        super(t);
    }

    public static State getObject(String str) {
        return stateImplJsonHelp.jsonToObject(str);
    }

    public static String toString(State stateImpl) {
        return stateImplJsonHelp.ObjToJson(stateImpl);
    }

}
