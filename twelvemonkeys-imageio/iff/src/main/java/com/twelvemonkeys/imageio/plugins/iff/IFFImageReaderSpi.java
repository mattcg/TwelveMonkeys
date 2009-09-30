/*
 * Copyright (c) 2008, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "TwelveMonkeys" nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.imageio.plugins.iff;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * IFFImageReaderSpi
 * <p/>
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @version $Id: IFFImageWriterSpi.java,v 1.0 28.feb.2006 19:21:05 haku Exp$
 */
public class IFFImageReaderSpi extends ImageReaderSpi {

    static IFFImageReaderSpi mSharedInstance;

    /**
     * Creates an IFFImageReaderSpi
     */
    public IFFImageReaderSpi() {
        super(
                "TwelveMonkeys",
                "2.0",
                new String[]{"iff", "IFF"},
                new String[]{"iff", "lbm", "ham", "ham8", "ilbm"},
                new String[]{"image/iff", "image/x-iff"},
                "com.twelvemonkeys.imageio.plugins.iff.IFFImageReader",
                STANDARD_INPUT_TYPE,
                new String[]{"com.twelvemonkeys.imageio.plugins.iff.IFFImageWriterSpi"},
                true, null, null, null, null,
                true, null, null, null, null
        );

        if (mSharedInstance == null) {
            mSharedInstance = this;
        }
    }

    public boolean canDecodeInput(Object pSource) throws IOException {
        return pSource instanceof ImageInputStream && canDecode((ImageInputStream) pSource);
    }

    private static boolean canDecode(ImageInputStream pInput) throws IOException {
        pInput.mark();

        try {
            // Is it IFF
            if (pInput.readInt() == IFF.CHUNK_FORM) {
                pInput.readInt();// Skip length field

                int type = pInput.readInt();

                // Is it ILBM or PBM
                if (type == IFF.TYPE_ILBM || type == IFF.TYPE_PBM) {
                    return true;
                }

            }
        }
        finally {
            pInput.reset();
        }

        return false;
    }


    public ImageReader createReaderInstance(Object pExtension) throws IOException {
        return new IFFImageReader(this);
    }

    public String getDescription(Locale pLocale) {
        return "Amiga (Electronic Arts) Image Interchange Format (IFF) image reader";
    }

    public static ImageReaderSpi sharedProvider() {
        if (mSharedInstance == null) {
            new IFFImageReaderSpi();
        }

        return mSharedInstance;
    }
}