# -*- mode: python ; coding: utf-8 -*-

block_cipher = None


a = Analysis(['Main.py'],
             pathex=['C:\\Users\\Trevor\\Documents\\eclipse-workspace\\TextingBot\\src\\App'],
             binaries=[('C:\\Users\\Trevor\\Documents\\eclipse-workspace\\TextingBot\\src\\App\\model.h5', '.')],
             datas=[('C:\\Users\\Trevor\\Documents\\eclipse-workspace\\TextingBot\\src\\App\\text.txt', '.'),
                    ('C:\\Users\\Trevor\\Documents\\eclipse-workspace\\TextingBot\\src\\App\\Media\\Apu No Background.png', 'Media'),
                    ('C:\\Users\\Trevor\\Documents\\eclipse-workspace\\TextingBot\\src\\App\\training_checkpoints\\checkpoint', '.')],
             hiddenimports=[],
             hookspath=[],
             runtime_hooks=[],
             excludes=[],
             win_no_prefer_redirects=False,
             win_private_assemblies=False,
             cipher=block_cipher,
             noarchive=False)
pyz = PYZ(a.pure, a.zipped_data,
             cipher=block_cipher)
exe = EXE(pyz,
          a.scripts,
          [],
          exclude_binaries=True,
          name='ApuChat',
          debug=False,
          bootloader_ignore_signals=False,
          strip=False,
          upx=False,
          console=False,
          icon='C:\\Users\\Trevor\\Documents\\eclipse-workspace\\TextingBot\\src\\App\\Media\\Apu Icon.ico')
coll = COLLECT(exe,
               a.binaries,
               a.zipfiles,
               a.datas,
               strip=False,
               upx=False,
               upx_exclude=[],
               name='ApuChat')
