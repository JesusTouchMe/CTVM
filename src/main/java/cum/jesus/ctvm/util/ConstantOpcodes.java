package cum.jesus.ctvm.util;

public final class ConstantOpcodes {
    public static final byte
            NOP = 0x00,
            NEWL = 0x0A,
            PUSH = 0x01,
            POP = 0x02,
            DUP = 0x03,
            ALCA = 0x04,
            FREA = 0x05,

            MOV = 0x07,
            MOVZ = 0x08,

            ALC = 0x09,
            FRE = 0x0B,
            LOD = 0x0C,
            STR = 0x0D,

            ADD = 0x10,
            SUB = 0x11,
            MUL = 0x12,
            DIV = 0x13,
            AND = 0x14,
            OR = 0x15,
            XOR = 0x16,
            SHL = 0x17,
            SHR = 0x18,
            LAND = 0x19,
            LOR = 0x1A,
            LXOR = 0x1B,

            INC = 0x1C,
            DEC = 0x1D,

            NOT = 0x1E,
            NEG = 0x1F,
            LNOT = 0x20,

            CMPEQ = 0x21,
            CMPNE = 0x22,
            CMPLT = 0x23,
            CMPGT = 0x24,
            CMPLTE = 0x25,
            CMPGTE = 0x26,

            JMP = 0x27,
            JMV = 0x28,
            JIT = 0x29,
            JVT = 0x2A,
            JIZ = 0x2B,
            JVZ = 0x2C,

            CALL = 0x2D,
            RET = 0x2E,
            INT = 0x2F,

            CLD = 0x30,
            CST = 0x31,

            MOD = 0x36,
            FUN = 0x37;
}
