/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.remote;

import org.apache.nifi.web.api.dto.remote.PeerDTO;

/**
 * This class represents the state of a specific peer, both its identifying information (contained
 * in the {@link PeerDescription}: hostname, port, and security) and its current status (number of
 * flowfiles and whether it can query other peers for status). Equality is only based on the
 * identifying information, so when iterating over multiple PeerStatus objects, more recent statuses
 * will replace previously acquired statuses for a specific peer.
 */
public class PeerStatus {

    private final PeerDescription description;
    private final int numFlowFiles;
    private final boolean queryForPeers;

    public PeerStatus(final PeerDescription description, final int numFlowFiles, final boolean queryForPeers) {
        this.description = description;
        this.numFlowFiles = numFlowFiles;
        this.queryForPeers = queryForPeers;
    }

    /**
     * Copy constructor from a {@link PeerDTO}. {@link #isQueryForPeers()} is hard-coded to {@code true}.
     *
     * @param peerDTO the peer DTO object with hostname, port, security, and flowfile count
     */
    public PeerStatus(final PeerDTO peerDTO) {
        this.description = new PeerDescription(peerDTO);
        this.numFlowFiles = peerDTO.getFlowFileCount();
        this.queryForPeers = true;
    }

    public PeerDescription getPeerDescription() {
        return description;
    }

    public int getFlowFileCount() {
        return numFlowFiles;
    }

    /**
     * @return <code>true</code> if this node can be queried for its peers, <code>false</code> otherwise.
     */
    public boolean isQueryForPeers() {
        return queryForPeers;
    }

    @Override
    public String toString() {
        return "PeerStatus[hostname=" + description.getHostname() + ",port=" + description.getPort()
                + ",secure=" + description.isSecure() + ",flowFileCount=" + numFlowFiles + "]";
    }

    @Override
    public int hashCode() {
        return 9824372 + description.getHostname().hashCode() + description.getPort() * 41;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof PeerStatus)) {
            return false;
        }

        final PeerStatus other = (PeerStatus) obj;
        return description.equals(other.getPeerDescription());
    }
}
