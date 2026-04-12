package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.cache.model.CachedSpace
import org.testadirapa.sesterzo.model.Space

interface SpacePersistentCache : PersistenceOperator<Space, CachedSpace>