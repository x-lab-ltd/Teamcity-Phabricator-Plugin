<?php

final class PassphraseTokenKey extends PassphraseAbstractKey {

    public static function loadFromPHID($phid, PhabricatorUser $viewer) {
      $key = new PassphraseTokenKey();
      return $key->loadAndValidateFromPHID(
          $phid,
          $viewer,
          PassphraseTokenCredentialType::PROVIDES_TYPE);
    }

    public function getPasswordEnvelope() {
      return $this->requireCredential()->getSecret();
    }

}