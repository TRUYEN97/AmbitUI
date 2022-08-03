# generate_product_serial.py
#
# This script will generate a 16 character product serial as defined by eero
# The generated product serial will be printed to the screen if successful
#
# usage:
#   python generate_product_serial.py <mfg_serial>
#
# requirements:
#   argparse==1.2.1
#   base32-crockford==0.3.0
#   crcmod==1.7
#
# This script has been tested wtih python 2.7.15

import argparse
import base32_crockford
import codecs
import crcmod.predefined
import random
import binascii

NUM_MANUFACTURING_SERIAL_BYTES = 5
NUM_RANDOM_BYTES = 4

crc_fn = crcmod.predefined.mkCrcFun('crc-8')


#def to_bytes(n, length, endianess='big'):
#    h = '%x' % n
#    s = ('0' * (len(h) % 2) + h).zfill(length * 2).decode('hex')
#    return s if endianess == 'big' else s[::-1]

def to_bytes(n, length, endianess='big'):
    h = '%x' % n
    s = codecs.decode(('0'*(len(h) % 2) + h).zfill(length*2), "hex_codec")
    return s if endianess == 'big' else s[::-1]


def from_bytes(data):
    return int(codecs.encode(data, 'hex'), 16)


def gen_one_product_serial(manufacturing_serial):
    """Generate a product serial"""
    decoded = to_bytes(
        base32_crockford.decode(manufacturing_serial),
        NUM_MANUFACTURING_SERIAL_BYTES,
        'big'
    )
    rand = random.getrandbits(NUM_RANDOM_BYTES * 8)
    intermediate = decoded + to_bytes(rand, NUM_RANDOM_BYTES, 'big')
    crc = crc_fn(intermediate)
    result = from_bytes(intermediate + bytearray([crc]))
    return base32_crockford.encode(result)


def verify_manufacturing_serial(manufacturing_serial):
    # manufacturing serial must be 8 characters long
    if len(manufacturing_serial) != 8:
        raise Exception("Given serial is not 8 characters long")


def gen_product_serial(manufacturing_serial):
    verify_manufacturing_serial(manufacturing_serial)
    product_serial = gen_one_product_serial (manufacturing_serial)
    return product_serial

    # omit = [c for c in OMITTED_CHARACTERS if c not in manufacturing_serial]
    # for _ in range(MAX_SERIAL_ATTEMPTS):
    #     product_serial = gen_one_product_serial(manufacturing_serial)
    #     if all(c not in product_serial for c in omit):
    #         return product_serial
    # raise Exception("Could not generate serial omitting (W, M) for manufacturing serial " +manufacturing_serial)


if __name__ == "__main__":
    parser = argparse.ArgumentParser("Generate Product Serial")
    parser.add_argument("mfg_serial", type=str, help="8 character manufacturing serial")
    args = parser.parse_args()
    print(gen_product_serial(args.mfg_serial))
    # print (gen_one_product_serial(args.mfg_serial))
