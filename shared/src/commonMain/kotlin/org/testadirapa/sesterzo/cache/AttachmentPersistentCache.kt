package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.cache.model.CachedAttachment
import org.testadirapa.sesterzo.model.EncryptedAttachment

interface AttachmentPersistentCache : PersistenceOperator<EncryptedAttachment, CachedAttachment>