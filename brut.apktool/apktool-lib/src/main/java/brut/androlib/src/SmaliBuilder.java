/**
 *  Copyright 2014 Ryszard Wiśniewski <brut.alll@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package brut.androlib.src;

import brut.androlib.AndrolibException;
import brut.androlib.mod.SmaliMod;
import brut.androlib.res.util.ExtFile;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.FileDataStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * @author Ryszard Wiśniewski <brut.alll@gmail.com>
 */
public class SmaliBuilder {

    public static void build(ExtFile smaliDir, File dexFile) throws AndrolibException {
        new SmaliBuilder(smaliDir, dexFile).build();
    }

    private SmaliBuilder(ExtFile smaliDir, File dexFile) {
        mSmaliDir = smaliDir;
        mDexFile = dexFile;
    }

    private void build() throws AndrolibException {
        try {
            DexBuilder dexBuilder = DexBuilder.makeDexBuilder();

            for (String fileName : mSmaliDir.getDirectory().getFiles(true)) {
                buildFile(fileName, dexBuilder);
            }
            dexBuilder.writeTo(new FileDataStore( new File(mDexFile.getAbsolutePath())));
        } catch (Exception ex) {
            throw new AndrolibException(ex);
        }
    }

    private void buildFile(String fileName, DexBuilder dexBuilder)
            throws AndrolibException, IOException {
        File inFile = new File(mSmaliDir, fileName);
        InputStream inStream = new FileInputStream(inFile);

        if (fileName.endsWith(".smali")) {
            try {
                if (!SmaliMod.assembleSmaliFile(inFile,dexBuilder, false, false)) {
                    throw new AndrolibException("Could not smali file: " + fileName);
                }
            } catch (Exception ex) {
                throw new AndrolibException(ex);
            }
        } else {
            LOGGER.warning("Unknown file type, ignoring: " + inFile);
        }
        inStream.close();
    }

    private final ExtFile mSmaliDir;
    private final File mDexFile;

    private final static Logger LOGGER = Logger.getLogger(SmaliBuilder.class.getName());
}
