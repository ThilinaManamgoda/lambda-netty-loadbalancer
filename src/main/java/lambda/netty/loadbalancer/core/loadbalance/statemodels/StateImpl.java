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

package lambda.netty.loadbalancer.core.loadbalance.statemodels;


import java.util.LinkedList;
import java.util.Queue;

public class StateImpl implements State {

    private Queue<String> hosts = new LinkedList<>();
    private InstanceStates state;

    public StateImpl() {
    }

    public InstanceStates getState() {
        return state;
    }

    public void setState(InstanceStates state) {
        this.state = state;
    }

    public Queue<String> getHosts() {

        return hosts;
    }

    public void pushHost(String host) {
        hosts.add(host);
    }

    public String pullHost() {
        return hosts.poll();
    }
}
